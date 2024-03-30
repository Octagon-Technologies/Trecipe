package com.octagontechnologies.trecipe.domain.recipe.nutrition

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Nutrient(
    val name: String, // Calories
    val amount: Double?, // 27.4
    val unit: String, // g
    private val percentOfDailyNeeds: Double? // 32.84
) : Parcelable {

    @IgnoredOnParcel
    val amountWithUnit = "$amount $unit"

    @IgnoredOnParcel
    val formattedPercentOfDailyNeeds = percentOfDailyNeeds?.let { "$percentOfDailyNeeds %" }

}