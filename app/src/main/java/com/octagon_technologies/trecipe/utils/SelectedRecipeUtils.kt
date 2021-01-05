package com.octagon_technologies.trecipe.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.models.State
import com.octagon_technologies.trecipe.repo.network.RemoteRecipeRepo
import com.octagon_technologies.trecipe.repo.network.models.selected_recipe.SelectedRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectedRecipeUtils @Inject constructor(private val remoteRecipeRepo: RemoteRecipeRepo) {

    private val _selectedRecipe = MutableLiveData<SelectedRecipe>()
    val selectedRecipe: LiveData<SelectedRecipe> = _selectedRecipe

    private val _state = MutableLiveData(State.Loading)
    val state: LiveData<State> = _state

    fun getSelectedRecipeFromRecipe(recipeId: Int?) {
        GlobalScope.launch(Dispatchers.Main.immediate) {
            remoteRecipeRepo.getSelectedRecipe(recipeId ?: return@launch, _state)
                .collect {
                    _selectedRecipe.value = it

                    if (_state.value != State.Done)
                        _state.value = State.Done
                }
        }
    }

    fun resetSelectedRecipe() {
        if (_selectedRecipe.value != null)
            _selectedRecipe.value = null
    }

    fun handleSelectedStateChanges(fragment: Fragment) {
        state.observe(fragment.viewLifecycleOwner) {
            if (it == State.NoNetworkError)
                ViewUtils.showShortSnackBar(fragment.requireView(), R.string.no_network_available)
            else if (it == State.ApiError)
                ViewUtils.showShortSnackBar(fragment.requireView(), R.string.api_limit_exceeded)
        }
    }
}

