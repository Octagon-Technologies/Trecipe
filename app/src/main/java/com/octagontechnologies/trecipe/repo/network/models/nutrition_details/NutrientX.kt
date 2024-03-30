package com.octagontechnologies.trecipe.repo.network.models.nutrition_details


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