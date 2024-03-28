package com.octagon_technologies.trecipe.presentation.ui.my_recipes

import androidx.lifecycle.ViewModel
import com.octagon_technologies.trecipe.repo.LocalRecipeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyRecipesViewModel @Inject constructor(
    private val localRecipeRepo: LocalRecipeRepo
) : ViewModel() {

    val likedRecipes = localRecipeRepo.likedRecipes
    val savedRecipes = localRecipeRepo.savedRecipes
    val recentRecipes = localRecipeRepo.recentRecipes

}