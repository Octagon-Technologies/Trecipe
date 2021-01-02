package com.octagon_technologies.foodie.repo.network.models.recipes


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Recipes(
    @Json(name = "recipes")
    val recipes: List<Recipe>?
)