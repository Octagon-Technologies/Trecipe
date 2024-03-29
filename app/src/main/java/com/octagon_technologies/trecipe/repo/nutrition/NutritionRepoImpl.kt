package com.octagon_technologies.trecipe.repo.nutrition

import com.octagon_technologies.trecipe.domain.Resource
import com.octagon_technologies.trecipe.domain.doOperation
import com.octagon_technologies.trecipe.repo.dto.toNutritionDetails
import com.octagon_technologies.trecipe.repo.network.RecipeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NutritionRepoImpl @Inject constructor(
    private val recipeApi: RecipeApi
) : NutritionRepo {

    override suspend fun fetchNutritionDetails(recipeID: Int) = doOperation {
        Resource.Success(recipeApi.getNutritionDetails(recipeID).toNutritionDetails())
    }

}