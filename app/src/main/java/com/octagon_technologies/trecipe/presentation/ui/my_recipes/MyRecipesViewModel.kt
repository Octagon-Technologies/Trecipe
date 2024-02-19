package com.octagon_technologies.trecipe.presentation.ui.my_recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.trecipe.models.BasicRoomRecipe
import com.octagon_technologies.trecipe.repo.database.LocalRecipeRepo
import com.octagon_technologies.trecipe.repo.network.RemoteRecipeRepo
import com.octagon_technologies.trecipe.repo.network.models.selected_recipe.SelectedRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecipesViewModel @Inject constructor(
    private val remoteRecipeRepo: RemoteRecipeRepo,
    private val localRecipeRepo: LocalRecipeRepo
) : ViewModel() {

    private val _likedRecipesList = MutableLiveData<List<BasicRoomRecipe>>()
    val likedRecipesList: LiveData<List<BasicRoomRecipe>> = _likedRecipesList

    private val _recentRecipesList = MutableLiveData<List<BasicRoomRecipe>>()
    val recentRecipesList: LiveData<List<BasicRoomRecipe>> = _recentRecipesList

    private val _downloadRecipeList = MutableLiveData<List<SelectedRecipe>>()
    val downloadRecipeList: LiveData<List<SelectedRecipe>> = _downloadRecipeList

    init {
        getLikedRecipes()
        getRecentRecipes()
        getDownloadRecipes()
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

    private fun getDownloadRecipes() {
        viewModelScope.launch {
            _downloadRecipeList.value = localRecipeRepo.getAllDownloadRecipes()
        }
    }

    fun deleteLikedRecipe(basicRoomRecipe: BasicRoomRecipe) {
        viewModelScope.launch {
            localRecipeRepo.deleteLikedRecipe(basicRoomRecipe)
        }
    }

    fun deleteRecentViewedRecipe(basicRoomRecipe: BasicRoomRecipe) {
        viewModelScope.launch {
            localRecipeRepo.deleteRecentRecipe(basicRoomRecipe)
        }
    }

    fun deleteDownloadRecipe(selectedRecipe: SelectedRecipe) {
        viewModelScope.launch {
            localRecipeRepo.deleteDownloadRecipe(selectedRecipe)
        }
    }
}