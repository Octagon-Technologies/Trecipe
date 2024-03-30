package com.octagontechnologies.trecipe.repo.network.models.selected_recipe


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
data class Measures(
    @Json(name = "metric")
    val metric: Metric,
    @Json(name = "us")
    val us: Us
): Parcelable