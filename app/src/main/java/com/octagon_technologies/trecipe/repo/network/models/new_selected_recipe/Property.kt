package com.octagon_technologies.trecipe.repo.network.models.new_selected_recipe


import com.squareup.moshi.Json

data class Property(
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "name")
    val name: String,
    @Json(name = "unit")
    val unit: String
)