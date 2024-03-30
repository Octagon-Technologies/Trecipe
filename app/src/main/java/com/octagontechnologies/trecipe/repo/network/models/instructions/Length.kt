package com.octagontechnologies.trecipe.repo.network.models.instructions


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Length(
    @Json(name = "number")
    var number: Int?,
    @Json(name = "unit")
    var unit: String?
)