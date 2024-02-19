package com.octagon_technologies.trecipe.presentation.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.trecipe.models.State
import com.octagon_technologies.trecipe.repo.network.RemoteRecipeRepo
import com.octagon_technologies.trecipe.repo.network.models.suggestions.RecipeSuggestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val remoteRecipeRepo: RemoteRecipeRepo) :
    ViewModel() {

    val query = MutableLiveData("")

    private val _recipeSuggestions = MutableLiveData<List<RecipeSuggestion>>()
    val recipeSuggestions: LiveData<List<RecipeSuggestion>> = _recipeSuggestions

    private val _state = MutableLiveData(State.Loading)
    val state: LiveData<State> = _state

    fun loadSuggestions(query: String) {
        viewModelScope.launch {
            remoteRecipeRepo.getRecipeSuggestionsFromQuery(query, _state)
                .collect {
                    _recipeSuggestions.value = it

                    if (it.isEmpty()) _state.value = State.Empty

                    if (_state.value != State.Done)
                        _state.value = State.Done
                }
        }
    }

}