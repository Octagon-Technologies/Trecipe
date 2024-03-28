package com.octagon_technologies.trecipe.domain.similar_recipe

import com.octagon_technologies.trecipe.utils.toRecipeTime
import kotlin.random.Random

data class SimilarRecipe(
    val id: Int,
    val imagePath: String? = null,
    val readyInMinutes: Int,
    val recipeName: String
) {

//    https://spoonacular.com/recipeImages/579247-240x150.jpg
    val image = imagePath?.let {  }

}

fun SimilarRecipe?.toRecipeTime(): String =
    this?.readyInMinutes.toRecipeTime()

// For now, we'll ve generating random ratings from 4.0 to 4.6 since we don't have a source of that
// data yet..... Yeah, a bit unethical but still.... :)
fun SimilarRecipe?.toRecipeRating() = Random.nextDouble(3.9, 4.7).toFloat()
