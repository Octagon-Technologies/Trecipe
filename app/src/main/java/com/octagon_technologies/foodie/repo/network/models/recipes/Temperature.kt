package com.octagon_technologies.foodie.repo.network.models.recipes


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Temperature(
    @Json(name = "number")
    val number: Double?,
    @Json(name = "unit")
    val unit: String?
): Parcelable