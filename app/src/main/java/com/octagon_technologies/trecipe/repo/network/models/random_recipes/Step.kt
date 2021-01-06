package com.octagon_technologies.trecipe.repo.network.models.random_recipes


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Step(
    @Json(name = "equipment")
    val equipment: List<Equipment>?,
    @Json(name = "ingredients")
    val ingredients: List<Ingredient>?,
    @Json(name = "length")
    val length: Length?,
    @Json(name = "number")
    val number: Int?,
    @Json(name = "step")
    val step: String?
) : Parcelable