package com.octagon_technologies.foodie.repo.network.models.recipes


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Equipment(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "image")
    val image: String?,
    @Json(name = "localizedName")
    val localizedName: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "temperature")
    val temperature: Temperature?
): Parcelable