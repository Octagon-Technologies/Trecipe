package com.octagon_technologies.foodie.repo.network.models.recipes


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Length(
    @Json(name = "number")
    val number: Int?,
    @Json(name = "unit")
    val unit: String?
) : Parcelable