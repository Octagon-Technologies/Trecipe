package com.octagontechnologies.trecipe.repo.network.models.random_recipes


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Recipes(
    @Json(name = "recipes")
    val randomRecipes: List<RandomRecipe>?
)