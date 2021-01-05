package com.octagon_technologies.trecipe.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.octagon_technologies.trecipe.R

object ViewUtils {

    fun loadGlideImage(imageView: ImageView, imageUrl: String?) {
        Glide.with(imageView)
            .load(imageUrl ?: return)
            .centerCrop()
            .into(imageView)
    }

    fun getHtmlFromString(dataToFormat: String?) =
        HtmlCompat.fromHtml(dataToFormat ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)

    fun showShortSnackBar(view: View?, message: String) {
        val context = view?.context ?: return
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ResUtils.getResColor(context, android.R.color.white))
            .setTextColor(ResUtils.getResColor(context, R.color.dark_blue))
            .show()
    }

    fun showShortSnackBar(view: View?, @StringRes stringRes: Int) =
        ViewUtils.showShortSnackBar(view, ResUtils.getResString(view?.context, stringRes))

    fun setupStatusBarAndNavigationBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }

        changeStatusBarColor(activity)
        changeStatusBarIcons(activity)
        changeSystemNavigationBarColor(activity)
    }

    private fun changeStatusBarIcons(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun changeStatusBarColor(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window?.statusBarColor = Color.WHITE
        }
    }

    private fun changeSystemNavigationBarColor(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window?.navigationBarColor = Color.WHITE
        }
    }
}