package com.octagontechnologies.trecipe.repo.nutrition

import com.octagontechnologies.trecipe.domain.Resource
import com.octagontechnologies.trecipe.domain.doOperation
import com.octagontechnologies.trecipe.repo.dto.toNutritionDetails
import com.octagontechnologies.trecipe.repo.network.RecipeApi
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