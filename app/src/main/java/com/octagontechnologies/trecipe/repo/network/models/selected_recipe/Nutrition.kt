package com.octagontechnologies.trecipe.repo.network.models.selected_recipe


import com.squareup.moshi.Json

data class Nutrition(
    @Json(name = "caloricBreakdown")
    val caloricBreakdown: CaloricBreakdown,
    @Json(name = "flavonoids")
    val remoteFlavonoids: List<RemoteFlavonoid>,
    @Json(name = "ingredients")
    val ingredients: List<IngredientX>,
    @Json(name = "nutrients")
    val nutrients: List<NutrientX>,
    @Json(name = "properties")
    val properties: List<Property>,
    @Json(name = "weightPerServing")
    val weightPerServing: WeightPerServing
)