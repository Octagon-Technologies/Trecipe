package com.octagon_technologies.trecipe.domain.recipe

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Nutrient(
    val name: String, // Calories
    val amount: Double?, // 27.4
    val unit: String, // g
    val percentOfDailyNeeds: Double // 32.84
) : Parcelable
