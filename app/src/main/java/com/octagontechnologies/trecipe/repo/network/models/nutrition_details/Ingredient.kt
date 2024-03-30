package com.octagontechnologies.trecipe.repo.network.models.nutrition_details


import com.squareup.moshi.Json

data class Ingredient(
    @Json(name = "amount")
    val amount: Double?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "nutrients")
    val nutrients: List<NutrientX>?,
    @Json(name = "unit")
    val unit: String?
)