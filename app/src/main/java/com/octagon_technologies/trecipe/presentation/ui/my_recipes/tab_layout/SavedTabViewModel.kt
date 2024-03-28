package com.octagon_technologies.trecipe.presentation.ui.my_recipes.tab_layout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe
import com.octagon_technologies.trecipe.repo.LocalRecipeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedTabViewModel @Inject constructor(
    private val localRecipeRepo: LocalRecipeRepo
): ViewModel() {

    fun isSaved(simpleRecipe: SimpleRecipe) = localRecipeRepo.isSaved(simpleRecipe.recipeId)
    fun saveOrUnSaveRecipe(simpleRecipe: SimpleRecipe) {
        viewModelScope.launch {localRecipeRepo.saveOrUnSaveRecipe(simpleRecipe) }
    }

}