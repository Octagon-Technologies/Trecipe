package com.octagon_technologies.trecipe.repo.dto

import com.octagon_technologies.trecipe.domain.discover.DiscoverRecipe
import com.octagon_technologies.trecipe.domain.recipe.Ingredient
import com.octagon_technologies.trecipe.domain.recipe.Step
import com.octagon_technologies.trecipe.domain.recipe.nutrition.Nutrient
import com.octagon_technologies.trecipe.domain.recipe.RecipeDetails
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe
import com.octagon_technologies.trecipe.domain.similar_recipe.SimilarRecipe
import com.octagon_technologies.trecipe.repo.network.models.new_selected_recipe.RemoteSelectedRecipe
import com.octagon_technologies.trecipe.repo.network.models.random_recipes.RandomRecipe
import com.octagon_technologies.trecipe.repo.network.models.similar_recipe.RemoteSimilarRecipe
import com.octagon_technologies.trecipe.utils.capitalize
import java.math.RoundingMode


fun RandomRecipe.toDiscoverRecipe() =
    DiscoverRecipe(
        recipeId = id,
        recipeImage = image,
        readyInMinutes = readyInMinutes,
        recipeName = title,
        recipeSummary = summary,
        recipeScore = spoonacularScore ?: 80.0
    )

// Converts DiscoverRecipe to SimilarRecipe for use in MiniRecipeGroup
fun DiscoverRecipe.toSimilarRecipe() =
    SimilarRecipe(recipeId, recipeImage, readyInMinutes, recipeName)

// Converts DiscoverRecipe to SimpleRecipe to be saved in Saved Database
fun DiscoverRecipe.toSimpleRecipe() =
    SimpleRecipe(recipeId, recipeName, recipeImage)

// Converts SimilarRecipe to SimpleRecipe to be saved in Saved Database
fun SimilarRecipe.toSimpleRecipe() =
    SimpleRecipe(id, recipeName, imagePath)

fun RemoteSimilarRecipe.toSimilarRecipe() =
    SimilarRecipe(
        id = id,
        imagePath = null,
        readyInMinutes = readyInMinutes,
        recipeName = title
    )

fun RemoteSelectedRecipe.toRecipeDetails() =
    with(this) {
        val steps =
            analyzedInstructions.first().steps.map { Step(it.number, it.step) }
        val listOfNutrition = nutrition.nutrients.map {
            Nutrient(
                name = it.name,
                amount = it.amount.toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble(),
                unit = it.unit,
                percentOfDailyNeeds = it.percentOfDailyNeeds.toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble(),
            )
        }

        val ingredients = extendedIngredients.map {
            val metric = it.measures.metric
            val us = it.measures.us

            Ingredient(
                id = it.id,
                name = (it.nameClean ?: it.name ?: it.originalName ?: it.original ?: "Secret ingredient").capitalize(),
                ingredientPath = it.image,
                metricAmount = metric.amount.toInt(),
                metricUnit = metric.unitShort,
                usAmount = us.amount.toInt(),
                usUnit = us.unitShort
            )
        }

        RecipeDetails(
            id,
            title,
            image,
            sourceName.capitalize(),
            summary,
            spoonacularScore,
            steps,
            listOfNutrition,
            ingredients,
            pricePerServing ?: 0.0,
            readyInMinutes,
            servings ?: 1,
            healthScore
        )
    }