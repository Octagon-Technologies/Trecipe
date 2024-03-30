package com.octagontechnologies.trecipe.repo.network.models.selected_recipe


import com.squareup.moshi.Json

data class Property(
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "name")
    val name: String,
    @Json(name = "unit")
    val unit: String
)