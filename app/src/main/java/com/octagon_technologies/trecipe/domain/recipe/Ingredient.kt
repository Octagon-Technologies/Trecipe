package com.octagon_technologies.trecipe.domain.recipe

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    val id: Int,
    val name: String,
    private val ingredientPath: String?,
    val metricAmount: Int,
    val metricUnit: String,
    val usAmount: Int,
    val usUnit: String
) : Parcelable {

    val ingredientImage = ingredientPath?.let { "https://spoonacular.com/cdn/ingredients_100x100/$ingredientPath" }
    fun toFormattedAmount(isUS: Boolean) =
        if (isUS) "$usAmount $usUnit"
        else "$metricAmount $metricUnit"

}
