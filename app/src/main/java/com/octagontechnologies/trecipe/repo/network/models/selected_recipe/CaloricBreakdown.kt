package com.octagontechnologies.trecipe.repo.network.models.selected_recipe


import com.squareup.moshi.Json

data class CaloricBreakdown(
    @Json(name = "percentCarbs")
    val percentCarbs: Double,
    @Json(name = "percentFat")
    val percentFat: Double,
    @Json(name = "percentProtein")
    val percentProtein: Double
)