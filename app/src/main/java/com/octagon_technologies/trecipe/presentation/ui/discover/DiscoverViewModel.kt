package com.octagon_technologies.trecipe.presentation.ui.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.trecipe.models.State
import com.octagon_technologies.trecipe.repo.network.RemoteRecipeRepo
import com.octagon_technologies.trecipe.repo.network.models.random_recipes.RandomRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val remoteRecipeRepo: RemoteRecipeRepo,
) : ViewModel() {
    private val _listOfRandomRecipes = MutableLiveData<List<RandomRecipe>>()
    val listOfRandomRecipe: LiveData<List<RandomRecipe>> = _listOfRandomRecipes

    private val _state = MutableLiveData(State.Loading)
    val state: LiveData<State> = _state

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            remoteRecipeRepo.getRandomRecipes(_state).collect {
                _listOfRandomRecipes.value = it?.distinct()

                // Check if the state is already done to avoid reassigning State.Done to _state.value
                // and if recipe list is empty then assign State.Empty
                if (_state.value != State.Done)
                    _state.value = State.Done
                else if (it?.isEmpty() == true)
                    _state.value = State.Empty
            }
        }
    }


}