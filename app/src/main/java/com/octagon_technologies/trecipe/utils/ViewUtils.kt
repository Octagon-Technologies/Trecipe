package com.octagon_technologies.trecipe.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.octagon_technologies.trecipe.R

fun ImageView.loadImage(imageUrl: String?, @DrawableRes defaultImage: Int) {
    Glide.with(this).apply {
        (if (imageUrl != null) load(imageUrl) else load(defaultImage))
            .placeholder(defaultImage)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
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