package com.octagon_technologies.trecipe.domain.recipe.nutrition

data class NutritionDetails(
    val nutrients: List<Nutrient>,
    val glycemic: Glycemic,
    val flavonoids: List<Flavonoid>
)
