package com.octagon_technologies.trecipe.repo.dto

import com.octagon_technologies.trecipe.domain.recipe.RecipeDetails
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe
import com.octagon_technologies.trecipe.repo.database.saved.SavedRecipeEntity
import com.octagon_technologies.trecipe.repo.database.liked.LikedRecipeEntity
import com.octagon_technologies.trecipe.repo.database.recent.RecentRecipeEntity

fun RecipeDetails.toSimpleRecipe() =
    SimpleRecipe(recipeId, recipeName, recipeImage)

fun RecipeDetails.toLikedRecipe() = LikedRecipeEntity(this.toSimpleRecipe())
fun RecipeDetails.toRecentRecipe() = RecentRecipeEntity(this.toSimpleRecipe())
fun SimpleRecipe.toSavedRecipe() = SavedRecipeEntity(this)