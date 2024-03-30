package com.octagontechnologies.trecipe.repo.nutrition

import com.octagontechnologies.trecipe.domain.Resource
import com.octagontechnologies.trecipe.domain.recipe.nutrition.NutritionDetails

interface NutritionRepo {

    /**
     * Fetches the nutrition details of the specific recipe
     *
     * @param recipeID - ID of the recipe to be fetched
     */
    suspend fun fetchNutritionDetails(recipeID: Int): Resource<NutritionDetails>

}