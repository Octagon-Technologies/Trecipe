package com.octagontechnologies.trecipe.utils

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.octagontechnologies.trecipe.presentation.MainActivity

object BottomNavUtils {

    fun showBottomNavView(fragmentActivity: FragmentActivity?) {
        getNavView(fragmentActivity).visibility = View.VISIBLE
    }

    fun hideBottomNavView(fragmentActivity: FragmentActivity?) {
        getNavView(fragmentActivity).visibility = View.GONE
    }

    fun getNavView(fragmentActivity: FragmentActivity?) =
        (fragmentActivity as MainActivity).binding.navView

}