package com.octagontechnologies.trecipe.repo.network.models.random_recipes


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Ingredient(
    @Json(name = "id")
    val id: Int?,
    @Json(name = "image")
    val image: String?,
    @Json(name = "localizedName")
    val localizedName: String?,
    @Json(name = "name")
    val name: String?
): Parcelable