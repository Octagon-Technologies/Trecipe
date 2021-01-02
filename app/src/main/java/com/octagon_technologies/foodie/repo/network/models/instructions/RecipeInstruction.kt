package com.octagon_technologies.foodie.repo.network.models.instructions


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeInstruction(
    @Json(name = "name")
    var name: String?,
    @Json(name = "steps")
    var steps: List<Step>?
)