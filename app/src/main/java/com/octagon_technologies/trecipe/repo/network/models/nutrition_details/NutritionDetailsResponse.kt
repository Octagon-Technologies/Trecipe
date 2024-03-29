package com.octagon_technologies.trecipe.repo.network.models.nutrition_details


import com.squareup.moshi.Json

data class NutritionDetailsResponse(
    @Json(name = "bad")
    val bad: List<Bad>?,
    @Json(name = "caloricBreakdown")
    val caloricBreakdown: CaloricBreakdown?,
    @Json(name = "calories")
    val calories: String?,
    @Json(name = "carbs")
    val carbs: String?,
    @Json(name = "expires")
    val expires: Long?,
    @Json(name = "fat")
    val fat: String?,
    @Json(name = "flavonoids")
    val remoteFlavonoids: List<RemoteFlavonoid>?,
    @Json(name = "good")
    val good: List<Good>?,
    @Json(name = "ingredients")
    val ingredients: List<Ingredient>?,
    @Json(name = "isStale")
    val isStale: Boolean?,
    @Json(name = "nutrients")
    val nutrients: List<NutrientX>?,
    @Json(name = "properties")
    val properties: List<Property>?,
    @Json(name = "protein")
    val protein: String?,
    @Json(name = "weightPerServing")
    val weightPerServing: WeightPerServing?
)