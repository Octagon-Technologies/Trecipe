package com.octagontechnologies.trecipe.repo

import com.octagontechnologies.trecipe.domain.ErrorType
import com.octagontechnologies.trecipe.domain.Resource
import com.octagontechnologies.trecipe.domain.doOperation
import com.octagontechnologies.trecipe.repo.dto.toRecipeDetails
import com.octagontechnologies.trecipe.repo.network.RecipeApi
import javax.inject.Inject

class SelectedRecipeRepo @Inject constructor(
    private val recipeApi: RecipeApi,
    private val localRecipeRepo: LocalRecipeRepo
) {

    suspend fun getSelectedRecipe(recipeId: Int) = doOperation {
        try {
            val recipeDetails = recipeApi.getSelectedRecipe(recipeId).toRecipeDetails()
            localRecipeRepo.insertRecentRecipe(recipeDetails)

            Resource.Success(recipeDetails)
        }
        // No steps in the recipe
        catch(e: NoSuchElementException) {
            Resource.Error(ErrorType.ApiError)
        }
    }

}