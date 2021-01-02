package com.octagon_technologies.foodie.presentation.ui.recipe

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.foodie.repo.database.LocalRecipeRepo
import com.octagon_technologies.foodie.repo.network.RemoteRecipeRepo
import com.octagon_technologies.foodie.repo.network.models.instructions.RecipeInstruction
import com.octagon_technologies.foodie.repo.network.models.selected_recipe.SelectedRecipe
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class RecipeViewModel @ViewModelInject constructor(
    private val remoteRecipeRepo: RemoteRecipeRepo,
    private val localRecipeRepo: LocalRecipeRepo
) : ViewModel() {
    private val _snackBarMessage = MutableLiveData<String>()
    val snackBarMessage: LiveData<String> = _snackBarMessage

    private val _recipeInstructions = MutableLiveData<List<RecipeInstruction>>()
    val recipeInstructions: LiveData<List<RecipeInstruction>> = _recipeInstructions

//    fun likeOrUnlikeRecipe() {
//        val selectedRecipe = selectedRecipe.value
//        Timber.d("selectedRecipe.value is $selectedRecipe")
//        if (selectedRecipe == null) return
//
//        viewModelScope.launch {
//            if (selectedRecipe.isLiked)
//                addToLikedRecipes(selectedRecipe)
//            else
//                removeFromLikedRecipes(selectedRecipe)
//        }
//    }
//
//    suspend fun addToLikedRecipes(selectedRecipe: SelectedRecipe) {
//        _snackBarMessage.value = "Added to liked recipes"
//        selectedRecipe.isLiked = true
//        localRecipeRepo.insertLikedRecipe(selectedRecipe)
//    }
//
//    fun removeFromLikedRecipes(selectedRecipe: SelectedRecipe) {
//        _snackBarMessage.value = "Removed from liked recipes"
//        selectedRecipe.isLiked = false
//        localRecipeRepo
//    }

    fun downloadRecipe(selectedRecipe: SelectedRecipe) {
        viewModelScope.launch {
            _snackBarMessage.value = "Download has started"
            Timber.d("downloadRecipe called in viewModel with recipe.id as ${selectedRecipe.id}")
            localRecipeRepo.insertDownloadRecipe(selectedRecipe)
        }
    }

    fun loadRecipeInstructions(selectedRecipe: SelectedRecipe) {
        viewModelScope.launch {
            remoteRecipeRepo
                .getRecipeInstructions(selectedRecipe.id ?: return@launch)
                .collect {
                    _recipeInstructions.value = it
                }
        }
    }

    fun resetSnackBarMessage() {
        _snackBarMessage.value = null
    }
}