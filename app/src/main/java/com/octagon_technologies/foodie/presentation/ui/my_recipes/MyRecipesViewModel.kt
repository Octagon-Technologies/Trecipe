package com.octagon_technologies.foodie.presentation.ui.my_recipes

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.foodie.models.BasicRoomRecipe
import com.octagon_technologies.foodie.repo.database.LocalRecipeRepo
import com.octagon_technologies.foodie.repo.network.RemoteRecipeRepo
import com.octagon_technologies.foodie.repo.network.models.selected_recipe.SelectedRecipe
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MyRecipesViewModel @ViewModelInject constructor(
    private val remoteRecipeRepo: RemoteRecipeRepo,
    private val localRecipeRepo: LocalRecipeRepo
) : ViewModel() {

    private val _selectedRecipe = MutableLiveData<SelectedRecipe>()
    val selectedRecipe: LiveData<SelectedRecipe> = _selectedRecipe

    private val _likedRecipesList = MutableLiveData<List<BasicRoomRecipe>>()
    val likedRecipesList: LiveData<List<BasicRoomRecipe>> = _likedRecipesList

    private val _recentRecipesList = MutableLiveData<List<BasicRoomRecipe>>()
    val recentRecipesList: LiveData<List<BasicRoomRecipe>> = _recentRecipesList

    init {
        getLikedRecipes()
        getRecentRecipes()
    }

    private fun getLikedRecipes() {
        viewModelScope.launch {
            _likedRecipesList.value = localRecipeRepo.getAllLikedRecipes()
        }
    }

    private fun getRecentRecipes() {
        viewModelScope.launch {
            _recentRecipesList.value = localRecipeRepo.getAllRecentRecipes()
        }
    }

    fun getSelectedRecipeFromRecipe(recipeId: Int) {
        viewModelScope.launch {
            remoteRecipeRepo.getSelectedRecipe(recipeId)
                .collect {
                    _selectedRecipe.value = it
                }
        }
    }
}