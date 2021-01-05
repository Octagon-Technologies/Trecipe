package com.octagon_technologies.trecipe.repo.network.models.random_recipes


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RandomRecipe(
    @Json(name = "aggregateLikes")
    val aggregateLikes: Int?,
    @Json(name = "analyzedInstructions")
    val analyzedInstructions: List<AnalyzedInstruction>?,
    @Json(name = "cheap")
    val cheap: Boolean?,
    @Json(name = "cookingMinutes")
    val cookingMinutes: Int?,
    @Json(name = "creditsText")
    val creditsText: String?,
    @Json(name = "cuisines")
    val cuisines: List<String>?,
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
    val instructions: String?,
    @Json(name = "license")
    val license: String?,
    @Json(name = "lowFodmap")
    val lowFodmap: Boolean?,
    @Json(name = "occasions")
    val occasions: List<String>?,
    @Json(name = "originalId")
    val originalId: Int?,
    @Json(name = "preparationMinutes")
    val preparationMinutes: Int?,
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
    @Json(name = "spoonacularSourceUrl")
    val spoonacularSourceUrl: String?,
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
    val weightWatcherSmartPoints: Int?
) : Parcelable