package com.octagontechnologies.trecipe.repo.network.models.selected_recipe


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Metric(
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "unitLong")
    val unitLong: String,
    @Json(name = "unitShort")
    val unitShort: String
) : Parcelable