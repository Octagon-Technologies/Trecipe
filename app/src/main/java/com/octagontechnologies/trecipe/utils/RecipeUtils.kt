package com.octagontechnologies.trecipe.utils

import com.octagontechnologies.trecipe.domain.recipe.RecipeDetails

// This .toRecipeTime() is used by both Recipe and SimilarRecipe hence abstracted to remove
// duplication of code which, as in the past, has led to errors
fun Int?.toRecipeTime(): String {
    val timeInMins = this
    return if (timeInMins != null) {
        if (timeInMins <= 60)
            "$timeInMins mins"
        else
            "${timeInMins / 60} hrs ${timeInMins % 60} mins"
    } else "-- mins"
}