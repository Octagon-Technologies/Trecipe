package com.octagon_technologies.foodie.presentation.ui.discover

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.foodie.repo.network.RemoteRecipeRepo
import com.octagon_technologies.foodie.repo.network.models.recipes.Recipe
import com.octagon_technologies.foodie.repo.network.models.selected_recipe.SelectedRecipe
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DiscoverViewModel @ViewModelInject constructor(private val remoteRecipeRepo: RemoteRecipeRepo) : ViewModel() {
    private val _listOfRecipes = MutableLiveData<List<Recipe>>()
    val listOfRecipe: LiveData<List<Recipe>> = _listOfRecipes

    private val _selectedRecipe = MutableLiveData<SelectedRecipe>()
    val selectedRecipe: LiveData<SelectedRecipe> = _selectedRecipe

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            remoteRecipeRepo.getRandomRecipes().collect {
                _listOfRecipes.value = it.recipes?.distinct()
            }
        }
    }

    fun getSelectedRecipeFromRecipe(recipe: Recipe) {
        viewModelScope.launch {
            remoteRecipeRepo.getSelectedRecipe(recipe.id ?: return@launch)
                .collect {
                    _selectedRecipe.value = it
                }
        }
    }

    fun resetSelectedRecipe() {
        _selectedRecipe.value = null
    }
}