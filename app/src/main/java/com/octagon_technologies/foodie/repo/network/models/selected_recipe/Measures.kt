package com.octagon_technologies.foodie.repo.network.models.selected_recipe


import android.os.Parcelable
import com.octagon_technologies.foodie.repo.network.models.recipes.TotalMetric
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Measures(
    @Json(name = "metric")
    val metric: TotalMetric?,
    @Json(name = "us")
    val us: TotalMetric?
) : Parcelable