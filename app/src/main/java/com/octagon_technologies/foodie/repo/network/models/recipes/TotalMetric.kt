package com.octagon_technologies.foodie.repo.network.models.recipes


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class TotalMetric(
    @Json(name = "amount")
    val amount: Double?,
    @Json(name = "unitLong")
    val unitLong: String?,
    @Json(name = "unitShort")
    val unitShort: String?
) : Parcelable