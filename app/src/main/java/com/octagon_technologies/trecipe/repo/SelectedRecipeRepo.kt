package com.octagon_technologies.trecipe.repo

import com.octagon_technologies.trecipe.domain.ErrorType
import com.octagon_technologies.trecipe.domain.Resource
import com.octagon_technologies.trecipe.domain.doOperation
import com.octagon_technologies.trecipe.repo.dto.toRecipeDetails
import com.octagon_technologies.trecipe.repo.network.RecipeApi
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