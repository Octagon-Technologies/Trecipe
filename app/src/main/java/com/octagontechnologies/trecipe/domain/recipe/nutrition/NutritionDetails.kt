package com.octagontechnologies.trecipe.domain.recipe.nutrition

data class NutritionDetails(
    val nutrients: List<Nutrient>,
    val glycemic: Glycemic,
    val flavonoids: List<Flavonoid>
)
