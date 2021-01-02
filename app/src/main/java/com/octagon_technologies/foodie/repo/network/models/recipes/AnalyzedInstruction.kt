package com.octagon_technologies.foodie.repo.network.models.recipes


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AnalyzedInstruction(
    @Json(name = "name")
    val name: String?,
    @Json(name = "steps")
    val steps: List<Step>?
) : Parcelable