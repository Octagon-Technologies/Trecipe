package com.octagon_technologies.foodie.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.octagon_technologies.foodie.ads.AdHelper

fun Fragment.adHelpers() = lazy {
    AdHelper(requireActivity() as AppCompatActivity)
}