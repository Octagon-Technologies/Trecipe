package com.octagon_technologies.trecipe.repo.network.models.selected_recipe


import com.squareup.moshi.Json

data class NutrientX(
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "name")
    val name: String,
    @Json(name = "percentOfDailyNeeds")
    val percentOfDailyNeeds: Double,
    @Json(name = "unit")
    val unit: String
)