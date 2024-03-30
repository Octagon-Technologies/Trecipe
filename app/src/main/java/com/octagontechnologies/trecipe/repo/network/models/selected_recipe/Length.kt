package com.octagontechnologies.trecipe.repo.network.models.selected_recipe


import com.squareup.moshi.Json

data class Length(
    @Json(name = "number")
    val number: Int,
    @Json(name = "unit")
    val unit: String
)