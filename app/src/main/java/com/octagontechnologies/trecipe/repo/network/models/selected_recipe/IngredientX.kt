package com.octagontechnologies.trecipe.repo.network.models.selected_recipe


import com.squareup.moshi.Json

data class IngredientX(
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "nutrients")
    val nutrients: List<NutrientX>,
    @Json(name = "unit")
    val unit: String
)