package com.octagon_technologies.trecipe.repo.network.models.nutrition_details


import com.squareup.moshi.Json

data class Good(
    @Json(name = "amount")
    val amount: String?,
    @Json(name = "indented")
    val indented: Boolean?,
    @Json(name = "percentOfDailyNeeds")
    val percentOfDailyNeeds: Double?,
    @Json(name = "title")
    val title: String?
)