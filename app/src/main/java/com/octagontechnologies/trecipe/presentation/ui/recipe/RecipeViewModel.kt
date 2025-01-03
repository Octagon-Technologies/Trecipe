package com.octagontechnologies.trecipe.presentation.ui.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.trecipe.domain.Resource
import com.octagontechnologies.trecipe.domain.recipe.RecipeDetails
import com.octagontechnologies.trecipe.domain.similar_recipe.SimilarRecipe
import com.octagontechnologies.trecipe.repo.LocalRecipeRepo
import com.octagontechnologies.trecipe.repo.RecipeRepo
import com.octagontechnologies.trecipe.repo.SelectedRecipeRepo
import com.octagontechnologies.trecipe.repo.dto.toSimpleRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    private val localRecipeRepo: LocalRecipeRepo,
    private val selectedRecipeRepo: SelectedRecipeRepo
) : ViewModel() {

    private val _recipeDetails = MutableLiveData<Resource<RecipeDetails>>(Resource.Loading())
    val recipeDetails: LiveData<Resource<RecipeDetails>> = _recipeDetails

    private val _isUS = MutableLiveData(false)
    val isUS: LiveData<Boolean> = _isUS

    private val _similarRecipes = MutableLiveData<Resource<List<SimilarRecipe>>>(Resource.Loading())
    val similarRecipes: LiveData<Resource<List<SimilarRecipe>>> = _similarRecipes



    fun loadRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            _recipeDetails.value = selectedRecipeRepo.getSelectedRecipe(recipeId)
            _similarRecipes.value = recipeRepo.getSimilarRecipes(recipeId)
        }
    }

    fun likeOrUnLikeRecipe() =
        viewModelScope.launch {
            localRecipeRepo.likeOrUnLikeRecipe(recipeDetails.value?.data ?: return@launch)
        }

    fun saveOrUnSaveRecipe() =
        viewModelScope.launch {
            localRecipeRepo.saveOrUnSaveRecipe(
                recipeDetails.value?.data?.toSimpleRecipe() ?: return@launch
            )
        }


    val isLiked = MediatorLiveData<Boolean>().apply {
        addSource(localRecipeRepo.likedRecipeIds) {
            value = it.checkIsLiked()
        }
        addSource(recipeDetails) {
            value = localRecipeRepo.likedRecipeIds.value.checkIsLiked()
        }
    }
    private fun List<Int>?.checkIsLiked() = this?.contains(recipeDetails.value?.data?.recipeId) == true


    val isSaved = MediatorLiveData<Boolean>().apply {
        val savedRecipes = localRecipeRepo.savedRecipesIds.asLiveData()
        addSource(savedRecipes) {
            value = it.checkIsSaved()
        }
        addSource(recipeDetails) {
            value = savedRecipes.value.checkIsSaved()
        }
    }
    private fun List<Int>?.checkIsSaved() = this?.contains(recipeDetails.value?.data?.recipeId) == true




    fun isSuggestedRecipeSaved(similarRecipe: SimilarRecipe) =
        localRecipeRepo.isSaved(similarRecipe.toSimpleRecipe().recipeId)

    fun saveOrUnSaveSuggestedRecipe(similarRecipe: SimilarRecipe) {
        viewModelScope.launch { localRecipeRepo.saveOrUnSaveRecipe(similarRecipe.toSimpleRecipe()) }
    }


    // Consider using .distinctUntilChanged() once we learn more about it
    fun changeUnitSystem(isUS: Boolean) {
        val currentIsUS = _isUS.value
        if (currentIsUS != isUS)
            _isUS.value = isUS
    }
}