package com.octagon_technologies.trecipe.domain.recipe.nutrition

import com.octagon_technologies.trecipe.repo.network.models.new_selected_recipe.RemoteFlavonoid

data class NutritionDetails(
    val nutrients: List<Nutrient>,
    val glycemic: Glycemic,
    val flavonoids: List<Flavonoid>
)
