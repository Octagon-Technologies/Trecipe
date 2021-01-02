package com.octagon_technologies.foodie.repo.network.models.selected_recipe


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Ingredient(
    @Json(name = "amount")
    val amount: Double?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "nutrients")
    val nutrients: List<Nutrient>?,
    @Json(name = "unit")
    val unit: String?
)