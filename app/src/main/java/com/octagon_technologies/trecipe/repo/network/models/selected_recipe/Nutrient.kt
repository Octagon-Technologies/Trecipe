package com.octagon_technologies.trecipe.repo.network.models.selected_recipe


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Nutrient(
    @Json(name = "amount")
    val amount: Double?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "unit")
    val unit: String?
)