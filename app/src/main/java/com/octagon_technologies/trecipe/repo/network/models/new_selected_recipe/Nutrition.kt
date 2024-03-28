package com.octagon_technologies.trecipe.repo.network.models.new_selected_recipe


import com.squareup.moshi.Json

data class Nutrition(
    @Json(name = "caloricBreakdown")
    val caloricBreakdown: CaloricBreakdown,
    @Json(name = "flavonoids")
    val flavonoids: List<Flavonoid>,
    @Json(name = "ingredients")
    val ingredients: List<IngredientX>,
    @Json(name = "nutrients")
    val nutrients: List<NutrientX>,
    @Json(name = "properties")
    val properties: List<Property>,
    @Json(name = "weightPerServing")
    val weightPerServing: WeightPerServing
)