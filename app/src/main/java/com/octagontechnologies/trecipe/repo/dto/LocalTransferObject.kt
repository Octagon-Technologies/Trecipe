package com.octagontechnologies.trecipe.repo.dto

import com.octagontechnologies.trecipe.domain.recipe.RecipeDetails
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe
import com.octagontechnologies.trecipe.repo.database.saved.SavedRecipeEntity
import com.octagontechnologies.trecipe.repo.database.liked.LikedRecipeEntity
import com.octagontechnologies.trecipe.repo.database.recent.RecentRecipeEntity

fun RecipeDetails.toSimpleRecipe() =
    SimpleRecipe(recipeId, recipeName, recipeImage)

fun RecipeDetails.toLikedRecipe() = LikedRecipeEntity(this.toSimpleRecipe())
fun RecipeDetails.toRecentRecipe() = RecentRecipeEntity(this.toSimpleRecipe())
fun SimpleRecipe.toSavedRecipe() = SavedRecipeEntity(this)