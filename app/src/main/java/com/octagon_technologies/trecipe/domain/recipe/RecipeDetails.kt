package com.octagon_technologies.trecipe.domain.recipe

import android.os.Parcelable
import com.octagon_technologies.trecipe.domain.recipe.nutrition.Nutrient
import com.octagon_technologies.trecipe.utils.lowercase
import com.octagon_technologies.trecipe.utils.toRecipeTime
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeDetails(
    val recipeId: Int,
    val recipeName: String,
    val recipeImage: String?,
    val recipeAuthor: String,
    val recipeSummary: String?,
    internal val recipeScore: Double,
    val steps: List<Step>,
    val nutrition: List<Nutrient>,
    val ingredients: List<Ingredient>,
    val pricePerServing: Double?,
    val readyInMinutes: Int?,
    val servings: Int,
    val healthScore: Int?
) : Parcelable

fun RecipeDetails?.toRecipeName() =
    this?.recipeName ?: "--"

fun RecipeDetails?.toRecipeAuthor() =
    this?.recipeAuthor ?: "--"

fun RecipeDetails?.toRecipeSummary() =
    this?.recipeSummary?.split(". ")?.filterNot { it.contains("<a") }?.joinToString(".") ?: ""

// Converts the out of 100 score to a rating that can be displayed to the user as well as used to run
// the rating bar
fun RecipeDetails?.toRecipeRating(): Double =
    this?.recipeScore?.div(20) ?: 4.0

fun RecipeDetails?.toRecipeTime(): String = this?.readyInMinutes.toRecipeTime()

fun RecipeDetails?.toCalories(): Nutrient =
    this?.nutrition?.firstOrNull { it.name.lowercase() == "calories" }
        ?: Nutrient("Calories", null, "g", 0.0)

fun RecipeDetails?.toFat(): Nutrient =
    this?.nutrition?.firstOrNull { it.name.lowercase() == "fat" }
        ?: Nutrient("Fat", null, "g", 0.0)

fun RecipeDetails?.toProtein(): Nutrient =
    this?.nutrition?.firstOrNull { it.name.lowercase() == "protein" }
        ?: Nutrient("Protein", null, "g", 0.0)

fun RecipeDetails?.toCarbs(): Nutrient =
    this?.nutrition?.firstOrNull { it.name.lowercase() == "carbs" }
        ?: Nutrient("Carbs", null, "g", 0.0)