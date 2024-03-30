package com.octagontechnologies.trecipe.repo.network.models.selected_recipe


import com.squareup.moshi.Json

data class WeightPerServing(
    @Json(name = "amount")
    val amount: Int,
    @Json(name = "unit")
    val unit: String
)