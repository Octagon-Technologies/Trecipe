package com.octagontechnologies.trecipe.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.octagontechnologies.trecipe.R

fun ImageView.loadImage(imageUrl: String?, @DrawableRes defaultImage: Int) {
    Glide.with(this).apply {
        (if (imageUrl != null) load(imageUrl) else load(defaultImage))
            .placeholder(defaultImage)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
//            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(this@loadImage)
    }
}

fun Fragment.showShortSnackBar(@StringRes stringRes: Int) =
    showShortSnackBar(ResUtils.getResString(requireView().context, stringRes))

fun Fragment.showShortSnackBar(message: String) {
    val context = view?.context ?: return

    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(ContextCompat.getColor(context, R.color.theme_blue))
        .setTextColor(ContextCompat.getColor(context, android.R.color.white))
        .show()
}


class CustomAnimator : DefaultItemAnimator() {

    override fun onRemoveStarting(item: RecyclerView.ViewHolder?) {
//        super.onRemoveStarting(item)
        val holder = item?.itemView

        holder?.clearAnimation()
        holder?.animate()?.apply {
            alpha(0f)
            duration = 1500
            interpolator = AccelerateInterpolator()

            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    dispatchRemoveFinished(item)
                }
            })

            start()
        }
    }

    override fun onRemoveFinished(item: RecyclerView.ViewHolder?) {
        super.onRemoveFinished(item)

    }
}

/**
 * Copy pasted this animation class from the Legendary StackOverflow
 *
 * Not exactly what we are looking for but...
 * ..... I don't even understand the code in the first place :(
 */
class StackAnimator : ItemAnimator() {
    // must keep track of all pending/ongoing animations.
    private val pending = ArrayList<AnimInfo>()
    private val disappearances = HashMap<RecyclerView.ViewHolder, AnimInfo>()
    private val persistences = HashMap<RecyclerView.ViewHolder, AnimInfo>()
    
    override fun animateDisappearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo?
    ): Boolean {
        pending.add(AnimInfo(viewHolder, ANIMATION_TYPE_DISAPPEAR, 0f))
        dispatchAnimationStarted(viewHolder)
        // new pending animation added, return true to indicate we want a call to runPendingAnimations()
        return true
    }

    override fun animateAppearance(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo?,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        dispatchAnimationFinished(viewHolder)
        return false
    }

    override fun animatePersistence(
        viewHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        if (preLayoutInfo.top != postLayoutInfo.top) {
            // required movement
            val topDiff = preLayoutInfo.top - postLayoutInfo.top
            val per = persistences[viewHolder]
            if (per != null && per.isRunning) {
                // there is already an ongoing animation - update it instead
                per.top = per.holder.itemView.translationY + topDiff
                per.start()
                // discard this animatePersistence call
                dispatchAnimationFinished(viewHolder)
                return false
            }
            pending.add(AnimInfo(viewHolder, ANIMATION_TYPE_MOVE, topDiff.toFloat()))
            dispatchAnimationStarted(viewHolder)
            // new pending animation added, return true to indicate we want a call to runPendingAnimations()
            return true
        }
        dispatchAnimationFinished(viewHolder)
        return false
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preLayoutInfo: ItemHolderInfo,
        postLayoutInfo: ItemHolderInfo
    ): Boolean {
        dispatchAnimationFinished(oldHolder)
        dispatchAnimationFinished(newHolder)
        return false
    }

    override fun runPendingAnimations() {
        for (ai in pending) {
            ai.start()
        }
        pending.clear()
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        var ai = disappearances[item]
        if (ai != null && ai.isRunning) {
            ai.holder.itemView.animate().cancel()
        }
        ai = persistences[item]
        if (ai != null && ai.isRunning) {
            ai.holder.itemView.animate().cancel()
        }
    }

    override fun endAnimations() {
        for (ai in disappearances.values) if (ai.isRunning) ai.holder.itemView.animate().cancel()
        for (ai in persistences.values) if (ai.isRunning) ai.holder.itemView.animate().cancel()
    }

    override fun isRunning(): Boolean {
        return (pending.isNotEmpty()
                || disappearances.isNotEmpty()
                || persistences.isNotEmpty())
    }

    /**
     * This is container for each animation. It's also cancel/end listener for them.
     */
    inner class AnimInfo(
        val holder: RecyclerView.ViewHolder,
        private val animationType: Int,
        var top: Float
    ) :
        Animator.AnimatorListener {
        var isRunning = false
        fun start() {
            val itemView = holder.itemView
            itemView.animate().setListener(this)
            when (animationType) {
                ANIMATION_TYPE_DISAPPEAR -> {
                    itemView.pivotY = 0f
                    itemView.animate().scaleX(0f).scaleY(0f).setDuration(removeDuration)
                    disappearances[holder] = this // must keep track of all animations
                }

                ANIMATION_TYPE_MOVE -> {
                    itemView.translationY = top
                    itemView.animate().translationY(0f).setDuration(moveDuration)
                    persistences[holder] = this // must keep track of all animations
                }
            }
            isRunning = true
        }

        private fun resetViewHolderState() {
            // reset state as if no animation was ran
            when (animationType) {
                ANIMATION_TYPE_DISAPPEAR -> {
                    holder.itemView.scaleX = 1f
                    holder.itemView.scaleY = 1f
                }

                ANIMATION_TYPE_MOVE -> holder.itemView.translationY = 0f
            }
        }

        override fun onAnimationEnd(animation: Animator) {
            when (animationType) {
                ANIMATION_TYPE_DISAPPEAR -> disappearances.remove(
                    holder
                )

                ANIMATION_TYPE_MOVE -> persistences.remove(holder)
            }
            resetViewHolderState()
            holder.itemView.animate().setListener(null) // clear listener
            dispatchAnimationFinished(holder)
            if (!isRunning()) dispatchAnimationsFinished()
            isRunning = false
        }

        override fun onAnimationCancel(animation: Animator) {
            // jump to end state
            when (animationType) {
                ANIMATION_TYPE_DISAPPEAR -> {
                    holder.itemView.scaleX = 0f
                    holder.itemView.scaleY = 0f
                }

                ANIMATION_TYPE_MOVE -> holder.itemView.translationY = 0f
            }
        }

        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    }

    companion object {
        private const val ANIMATION_TYPE_DISAPPEAR = 1
        private const val ANIMATION_TYPE_MOVE = 2
    }
}