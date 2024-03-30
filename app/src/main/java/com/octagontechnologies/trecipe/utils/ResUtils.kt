package com.octagontechnologies.trecipe.utils

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

object ResUtils {

    fun getResString(context: Context?, @StringRes stringRes: Int) =
        context?.getString(stringRes) ?: ""

    fun getPixelsFromSdp(context: Context, sdpInt: Int) =
        context.resources.getDimensionPixelOffset(sdpInt)

}

fun Context.getResColorStateList(@ColorRes colorRes: Int) =
    ColorStateList.valueOf(ContextCompat.getColor(this, colorRes))