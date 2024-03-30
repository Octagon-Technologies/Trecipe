package com.octagontechnologies.trecipe.repo.network.models.complex_search


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResult(
    @Json(name = "id")
    var id: Int,
    @Json(name = "image")
    var image: String,
    @Json(name = "imageType")
    var imageType: String?,
    @Json(name = "title")
    var title: String
)