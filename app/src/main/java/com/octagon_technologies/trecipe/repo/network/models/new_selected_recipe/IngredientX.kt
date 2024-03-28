package com.octagon_technologies.trecipe.repo.network.models.new_selected_recipe


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