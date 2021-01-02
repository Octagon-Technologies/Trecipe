package com.octagon_technologies.foodie.repo.network.models.selected_recipe


import com.octagon_technologies.foodie.models.BasicRoomRecipe
import com.octagon_technologies.foodie.repo.network.models.recipes.ExtendedIngredient
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class SelectedRecipe(
    @Json(name = "aggregateLikes")
    val aggregateLikes: Int?,
    @Json(name = "analyzedInstructions")
    val analyzedInstructions: List<Any>?,
    @Json(name = "cheap")
    val cheap: Boolean?,
    @Json(name = "creditsText")
    val creditsText: String?,
    @Json(name = "cuisines")
    val cuisines: List<Any>?,
    @Json(name = "dairyFree")
    val dairyFree: Boolean?,
    @Json(name = "diets")
    val diets: List<String>?,
    @Json(name = "dishTypes")
    val dishTypes: List<String>?,
    @Json(name = "extendedIngredients")
    val extendedIngredients: List<ExtendedIngredient>?,
    @Json(name = "gaps")
    val gaps: String?,
    @Json(name = "glutenFree")
    val glutenFree: Boolean?,
    @Json(name = "healthScore")
    val healthScore: Double?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "image")
    val image: String?,
    @Json(name = "imageType")
    val imageType: String?,
    @Json(name = "instructions")
    val instructions: Any?,
    @Json(name = "lowFodmap")
    val lowFodmap: Boolean?,
    @Json(name = "nutrition")
    val nutrition: Nutrition?,
    @Json(name = "occasions")
    val occasions: List<Any>?,
    @Json(name = "originalId")
    val originalId: Any?,
    @Json(name = "pricePerServing")
    val pricePerServing: Double?,
    @Json(name = "readyInMinutes")
    val readyInMinutes: Int?,
    @Json(name = "servings")
    val servings: Int?,
    @Json(name = "sourceName")
    val sourceName: String?,
    @Json(name = "sourceUrl")
    val sourceUrl: String?,
    @Json(name = "spoonacularScore")
    val spoonacularScore: Double?,
    @Json(name = "summary")
    val summary: String?,
    @Json(name = "sustainable")
    val sustainable: Boolean?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "vegan")
    val vegan: Boolean?,
    @Json(name = "vegetarian")
    val vegetarian: Boolean?,
    @Json(name = "veryHealthy")
    val veryHealthy: Boolean?,
    @Json(name = "veryPopular")
    val veryPopular: Boolean?,
    @Json(name = "weightWatcherSmartPoints")
    val weightWatcherSmartPoints: Int?,
    @Json(name = "winePairing")
    val winePairing: WinePairing?
) : Serializable {

    var isLiked = false
    var isRecent = false

    fun toBasicRoomRecipe(): BasicRoomRecipe =
        BasicRoomRecipe(id ?: -1, title ?: "", image ?: "")
}