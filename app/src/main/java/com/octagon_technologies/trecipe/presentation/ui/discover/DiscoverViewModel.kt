package com.octagon_technologies.trecipe.presentation.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.trecipe.domain.Resource
import com.octagon_technologies.trecipe.domain.discover.DiscoverRecipe
import com.octagon_technologies.trecipe.repo.LocalRecipeRepo
import com.octagon_technologies.trecipe.repo.RecipeRepo
import com.octagon_technologies.trecipe.repo.dto.toSimpleRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    private val localRecipeRepo: LocalRecipeRepo
) : ViewModel() {

    private val _dailyRecipesResult = MutableLiveData<Resource<List<DiscoverRecipe>>>(Resource.Loading())
    val dailyRecipesResult: LiveData<Resource<List<DiscoverRecipe>>> = _dailyRecipesResult

    private val _tryRecipesResult = MutableLiveData<Resource<List<DiscoverRecipe>>>(Resource.Loading())
    val tryRecipesResult: LiveData<Resource<List<DiscoverRecipe>>> = _tryRecipesResult


    init {
        viewModelScope.launch {
            _dailyRecipesResult.value = Resource.Loading()
            _tryRecipesResult.value = Resource.Loading()

            fetchDailyRecipes()
            fetchTryOutRecipes()
        }
    }

    private fun fetchDailyRecipes() =
        viewModelScope.launch { _dailyRecipesResult.value = recipeRepo.getDiscoverRecipes() }
    fun fetchTryOutRecipes() =
        viewModelScope.launch { _tryRecipesResult.value = recipeRepo.getTryOutRecipes() }

    fun isSaved(discoverRecipe: DiscoverRecipe) =
        localRecipeRepo.isSaved(discoverRecipe.recipeId)

    fun saveOrUnSaveRecipe(discoverRecipe: DiscoverRecipe) =
        viewModelScope.launch {
            localRecipeRepo.saveOrUnSaveRecipe(discoverRecipe.toSimpleRecipe())
        }


}