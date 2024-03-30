package com.octagontechnologies.trecipe.presentation.ui.my_recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe
import com.octagontechnologies.trecipe.repo.LocalRecipeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecipesViewModel @Inject constructor(
    private val localRecipeRepo: LocalRecipeRepo
) : ViewModel() {

    val likedRecipes = localRecipeRepo.likedRecipes.asFlow()
    val savedRecipes = localRecipeRepo.savedRecipes
    val recentRecipes = localRecipeRepo.recentRecipes.asFlow()

    fun removeFromRecent(simpleRecipe: SimpleRecipe) = viewModelScope.launch {
        localRecipeRepo.removeFromRecent(simpleRecipe.recipeId)
    }

    fun removeFromLiked(simpleRecipe: SimpleRecipe) = viewModelScope.launch {
        localRecipeRepo.removeFromLiked(simpleRecipe.recipeId)
    }

    fun removeFromSaved(simpleRecipe: SimpleRecipe) = viewModelScope.launch {
        localRecipeRepo.removeFromSaved(simpleRecipe.recipeId)
    }
}