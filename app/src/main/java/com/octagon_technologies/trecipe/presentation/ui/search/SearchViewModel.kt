package com.octagon_technologies.trecipe.presentation.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.trecipe.domain.State
import com.octagon_technologies.trecipe.domain.search.AutoComplete
import com.octagon_technologies.trecipe.repo.RecipeRepo
import com.octagon_technologies.trecipe.repo.SearchRepo
import com.octagon_technologies.trecipe.utils.tryCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val recipeRepo: RecipeRepo,
    private val searchRepo: SearchRepo
) : ViewModel() {

    private val _listOfRecentAutoComplete = MutableLiveData<List<AutoComplete>>()
    val listOfRecentAutoComplete: LiveData<List<AutoComplete>> = _listOfRecentAutoComplete

    private val _recipeSuggestions = MutableLiveData<List<AutoComplete>>()
    val recipeSuggestions: LiveData<List<AutoComplete>> = _recipeSuggestions

    private val _state = MutableLiveData(State.Loading)
    val state: LiveData<State> = _state

    private val _navigateToSearchResults = MutableLiveData<String?>()
    val navigateToSearchResults: LiveData<String?> = _navigateToSearchResults

    init {
        viewModelScope.launch {
            _listOfRecentAutoComplete.value = searchRepo.getRecentAutoComplete()
        }
    }

    fun loadSuggestions(query: String) {
        viewModelScope.launch {
            _state.tryCatching {
                _recipeSuggestions.value = searchRepo.getAutoCompleteSuggestions(query)
                Timber.d("Suspended till recipeRepo.getAutoCompleteSuggestions(query) finished")
            }

            // We'll wait till we get the network result; if we received a list but it was empty
            // it means the user's search query isn't on our Database
            Timber.d("About to move to if check: ${recipeSuggestions.value?.isEmpty()}")
            if (recipeSuggestions.value?.isEmpty() == true) _state.value = State.Empty
        }
    }

    fun addQueryToRecent(autoComplete: AutoComplete) {
        viewModelScope.launch {
            searchRepo.insertAutoComplete(autoComplete)
            _navigateToSearchResults.value = autoComplete.name
        }
    }

    fun resetNavigateToSearchResults() {
        _navigateToSearchResults.value = null
    }
}