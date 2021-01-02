package com.octagon_technologies.foodie.repo.network.models.selected_recipe


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Nutrition(
    @Json(name = "caloricBreakdown")
    val caloricBreakdown: CaloricBreakdown?,
    @Json(name = "flavanoids")
    val flavors: List<Flavanoid>?,
    @Json(name = "ingredients")
    val ingredients: List<Ingredient>?,
    @Json(name = "nutrients")
    val nutrients: List<NutrientX>?,
    @Json(name = "properties")
    val properties: List<Property>?,
    @Json(name = "weightPerServing")
    val weightPerServing: WeightPerServing?
)