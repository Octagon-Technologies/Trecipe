package com.octagontechnologies.trecipe.domain.discover

import com.octagontechnologies.trecipe.utils.toRecipeTime

// Special type of recipe that is displayed on the Discover Page... We map the detailed random recipes into this

/**
 *
 *
 * @param isSaved --> Has to be the biggest illegal workaround I've ever done... So, isSaved is
 * parameter that will be used in the Try out recipes paging recyclerview. We map the discover recipe,
 * check if the recipe ID is in the saved list, then update the isSaved property....
 * A lot of unnecessary work; probably bad code... Buhhhhhh, what's the worst that can happen
 */
data class DiscoverRecipe(
    val recipeId: Int,
    val recipeImage: String?,
    val readyInMinutes: Int,
    val recipeName: String,
    val recipeSummary: String,
    val recipeScore: Double,
    var isSaved: Boolean = false
)

fun DiscoverRecipe?.toRecipeTime(): String =
    this?.readyInMinutes.toRecipeTime()

fun DiscoverRecipe?.toRecipeRating(): Float = (this?.recipeScore?.div(20)?.coerceAtLeast(3.0) ?: 4.2).toFloat()
