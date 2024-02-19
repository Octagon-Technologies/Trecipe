package com.octagon_technologies.trecipe.presentation.ui.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.trecipe.models.BasicRoomRecipe
import com.octagon_technologies.trecipe.models.State
import com.octagon_technologies.trecipe.repo.database.LocalRecipeRepo
import com.octagon_technologies.trecipe.repo.network.RemoteRecipeRepo
import com.octagon_technologies.trecipe.repo.network.models.instructions.RecipeInstruction
import com.octagon_technologies.trecipe.repo.network.models.selected_recipe.SelectedRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val remoteRecipeRepo: RemoteRecipeRepo,
    private val localRecipeRepo: LocalRecipeRepo,
) : ViewModel() {
    private var initialLikedList = listOf<BasicRoomRecipe>()
    private var initialDownloadList = listOf<BasicRoomRecipe>()

    private val _snackBarMessage = MutableLiveData<String>()
    val snackBarMessage: LiveData<String> = _snackBarMessage

    private val _recipeInstructions = MutableLiveData<List<RecipeInstruction>>()
    val recipeInstructions: LiveData<List<RecipeInstruction>> = _recipeInstructions

    private val _isLiked = MutableLiveData(false)
    val isLiked: LiveData<Boolean> = _isLiked

    private val _isDownloaded = MutableLiveData(false)
    val isDownloaded: LiveData<Boolean> = _isDownloaded

    private val _state = MutableLiveData(State.Loading)
    val state: LiveData<State> = _state


    fun getLikedRecipes(selectedRecipe: SelectedRecipe) {
        viewModelScope.launch {
            initialLikedList = localRecipeRepo.getAllLikedRecipes()
            _isLiked.value = initialLikedList.contains(selectedRecipe.toBasicRoomRecipe())
        }
    }

    fun getDownloadRecipes(selectedRecipe: SelectedRecipe) {
        viewModelScope.launch {
            initialDownloadList =
                localRecipeRepo.getAllDownloadRecipes().map { it.toBasicRoomRecipe() }
            _isDownloaded.value = initialDownloadList.contains(selectedRecipe.toBasicRoomRecipe())
        }
    }

    fun likeOrDislikeRecipe() {
        _isLiked.value = _isLiked.value != true
    }

    fun addToRecentRecipes(selectedRecipe: SelectedRecipe) {
        viewModelScope.launch {
            localRecipeRepo.insertRecentRecipe(selectedRecipe)
        }
    }

    fun downloadOrDeleteRecipe(selectedRecipe: SelectedRecipe) {
        if (_isDownloaded.value == true)
            deleteRecipe(selectedRecipe)
        else
            downloadRecipe(selectedRecipe)
    }

    private fun downloadRecipe(selectedRecipe: SelectedRecipe) {
        viewModelScope.launch {
            val recipeInstructions = _recipeInstructions.value
            if (recipeInstructions == null) {
                _snackBarMessage.value = "Download failed"
                return@launch
            }

            _snackBarMessage.value = "Download has started"
            _isDownloaded.value = true

            Timber.d("downloadRecipe called in viewModel with recipe.id as ${selectedRecipe.id}")
            localRecipeRepo.insertDownloadRecipe(selectedRecipe, recipeInstructions)
        }
    }

    private fun deleteRecipe(selectedRecipe: SelectedRecipe) {
        viewModelScope.launch {
            localRecipeRepo.deleteDownloadRecipe(selectedRecipe)
            _isDownloaded.value = false
            _snackBarMessage.value = "Download has been deleted"
        }
    }

    fun loadRecipeInstructions(selectedRecipe: SelectedRecipe) {
        viewModelScope.launch {
            remoteRecipeRepo
                .getRecipeInstructions(selectedRecipe.id ?: return@launch, _state)
                .collect {
                    _recipeInstructions.value = it

                    if (_state.value != State.Done)
                        _state.value = State.Done
                }
        }
    }

    fun resetSnackBarMessage() {
        _snackBarMessage.value = null
    }

    fun saveRecipeLikeChanges(selectedRecipe: SelectedRecipe) {
        // Use GlobalScope instead of viewModelScope since onCleared() might be called soon hence
        // destroying the viewModelScope
        GlobalScope.launch(Dispatchers.IO) {
            val isAlreadyLiked = initialLikedList.contains(selectedRecipe.toBasicRoomRecipe())
            if (isAlreadyLiked) return@launch

            if (_isLiked.value == true)
                localRecipeRepo.insertLikedRecipe(selectedRecipe)
            else
                localRecipeRepo.deleteLikedRecipe(selectedRecipe.toBasicRoomRecipe())
        }
    }
}