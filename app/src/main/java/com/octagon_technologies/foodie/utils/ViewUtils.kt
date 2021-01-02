package com.octagon_technologies.foodie.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.octagon_technologies.foodie.R

object ViewUtils {

    fun getResColor(context: Context, @ColorRes colorRes: Int): Int =
        ContextCompat.getColor(context, colorRes)

    fun loadGlideImage(imageView: ImageView, imageUrl: String?) {
        Glide.with(imageView)
            .load(imageUrl ?: return)
            .into(imageView)
    }

    fun getHtmlFromString(dataToFormat: String?) =
        HtmlCompat.fromHtml(dataToFormat ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)

    fun showShortSnackBar(view: View?, message: String) {
        val context = view?.context ?: return
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(getResColor(context, R.color.dark_blue))
            .setTextColor(getResColor(context, android.R.color.white))
            .show()
    }

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