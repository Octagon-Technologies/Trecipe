package com.octagontechnologies.trecipe.repo.network.models.complex_search


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteComplexSearch(
    @Json(name = "number")
    var number: Int?,
    @Json(name = "offset")
    var offset: Int?,
    @Json(name = "results")
    var searchResults: List<SearchResult>,
    @Json(name = "totalResults")
    var totalResults: Int?
)