package com.octagon_technologies.trecipe.repo.network.models.suggestions


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RecipeSuggestion(
    @Json(name = "id")
    var id: Int?,
    @Json(name = "imageType")
    var imageType: String?,
    @Json(name = "title")
    var title: String?
): Parcelable