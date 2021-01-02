package com.octagon_technologies.foodie.repo.network.models.selected_recipe


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Flavanoid(
    @Json(name = "amount")
    val amount: Double?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "unit")
    val unit: String?
)