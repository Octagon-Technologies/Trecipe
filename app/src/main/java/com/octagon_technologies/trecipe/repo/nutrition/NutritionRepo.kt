package com.octagon_technologies.trecipe.repo.nutrition

import com.octagon_technologies.trecipe.domain.Resource
import com.octagon_technologies.trecipe.domain.recipe.nutrition.NutritionDetails

interface NutritionRepo {

    /**
     * Fetches the nutrition details of the specific recipe
     *
     * @param recipeID - ID of the recipe to be fetched
     */
    suspend fun fetchNutritionDetails(recipeID: Int): Resource<NutritionDetails>

}