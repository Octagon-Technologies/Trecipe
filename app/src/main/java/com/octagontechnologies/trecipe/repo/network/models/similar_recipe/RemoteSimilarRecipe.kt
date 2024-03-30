package com.octagontechnologies.trecipe.repo.network.models.similar_recipe


import com.squareup.moshi.Json

data class RemoteSimilarRecipe(
    @Json(name = "id")
    val id: Int,
    @Json(name = "imageType")
    val imageType: String,
    @Json(name = "readyInMinutes")
    val readyInMinutes: Int,
    @Json(name = "servings")
    val servings: Int,
    @Json(name = "sourceUrl")
    val sourceUrl: String,
    @Json(name = "title")
    val title: String
)