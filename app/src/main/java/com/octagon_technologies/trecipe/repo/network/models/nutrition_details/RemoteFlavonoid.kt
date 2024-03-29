package com.octagon_technologies.trecipe.repo.network.models.nutrition_details


import com.squareup.moshi.Json

data class RemoteFlavonoid(
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "name")
    val name: String,
    @Json(name = "unit")
    val unit: String
)