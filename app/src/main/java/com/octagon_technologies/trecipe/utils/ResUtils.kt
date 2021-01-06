package com.octagon_technologies.trecipe.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

object ResUtils {

    fun getResString(context: Context?, @StringRes stringRes: Int) =
        context?.getString(stringRes) ?: ""

    fun getResColor(context: Context, @ColorRes colorRes: Int): Int =
        ContextCompat.getColor(context, colorRes)

    fun getPixelsFromSdp(context: Context, sdpInt: Int) =
        context.resources.getDimensionPixelOffset(sdpInt)

}