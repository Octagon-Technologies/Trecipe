package com.octagon_technologies.foodie.repo.network.models.instructions


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Step(
    @Json(name = "equipment")
    var equipment: List<Equipment>?,
    @Json(name = "ingredients")
    var ingredients: List<Ingredient>?,
    @Json(name = "length")
    var length: Length?,
    @Json(name = "number")
    var number: Int?,
    @Json(name = "step")
    var step: String?
)