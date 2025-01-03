package com.octagontechnologies.trecipe.repo.network.models.instructions


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Equipment(
    @Json(name = "id")
    var id: Int?,
    @Json(name = "image")
    var image: String?,
    @Json(name = "localizedName")
    var localizedName: String?,
    @Json(name = "name")
    var name: String?
)