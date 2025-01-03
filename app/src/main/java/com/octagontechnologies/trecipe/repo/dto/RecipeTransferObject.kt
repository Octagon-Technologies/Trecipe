package com.octagontechnologies.trecipe.repo.dto

import com.octagontechnologies.trecipe.domain.discover.DiscoverRecipe
import com.octagontechnologies.trecipe.domain.recipe.Ingredient
import com.octagontechnologies.trecipe.domain.recipe.Step
import com.octagontechnologies.trecipe.domain.recipe.nutrition.Nutrient
import com.octagontechnologies.trecipe.domain.recipe.RecipeDetails
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe
import com.octagontechnologies.trecipe.domain.similar_recipe.SimilarRecipe
import com.octagontechnologies.trecipe.repo.network.models.selected_recipe.RemoteSelectedRecipe
import com.octagontechnologies.trecipe.repo.network.models.random_recipes.RandomRecipe
import com.octagontechnologies.trecipe.repo.network.models.similar_recipe.RemoteSimilarRecipe
import com.octagontechnologies.trecipe.utils.capitalize
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
            analyzedInstructions?.first()?.steps?.map { Step(it.number, it.step) } ?: listOf()
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