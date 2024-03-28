package com.octagon_technologies.trecipe.repo.network.models.new_selected_recipe


import com.squareup.moshi.Json

data class Equipment(
    @Json(name = "id")
    val id: Int,
    @Json(name = "image")
    val image: String,
    @Json(name = "localizedName")
    val localizedName: String,
    @Json(name = "name")
    val name: String
)