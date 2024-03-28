package com.octagon_technologies.trecipe.presentation.ui.search_results

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.trecipe.domain.State
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe
import com.octagon_technologies.trecipe.repo.RecipeRepo
import com.octagon_technologies.trecipe.repo.SearchRepo
import com.octagon_technologies.trecipe.utils.tryCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val searchRepo: SearchRepo
) : ViewModel() {
    // Helps us know how many recipes have been fetched
    private var offset = 0
    private var lastSearchedQuery = ""

    private val _state = MutableLiveData(State.Loading)
    val state: LiveData<State> = _state

    private val _listOfSearchResult = MutableLiveData<List<SimpleRecipe>>()
    val listOfSearchResult: LiveData<List<SimpleRecipe>> = _listOfSearchResult

    fun loadComplexSearch(query: String) {
        viewModelScope.launch {
            _state.tryCatching {
                // If we are searching something new, start from offset 0;
                // If we are loading up more recipes (same search query as before), continue from the offset
                if (lastSearchedQuery != query)
                    offset = 0

                _listOfSearchResult.value = searchRepo.getRecipesBasedOnSearch(query, offset)
                lastSearchedQuery = query

                offset += 10

                if (_listOfSearchResult.value.isNullOrEmpty())
                    _state.value = State.Empty
                else
                    _state.value = State.Done
            }

            Timber.d("State is ${state.value}")
        }
    }

    fun checkIfShouldReloadMore(query: String, lastItemPosition: Int) {
        // If the list is empty or null, the diff will be 10 hence no data fetched
        val searchResultsSize = listOfSearchResult.value?.size ?: 10
        val diff = searchResultsSize - lastItemPosition

        // If the number of unscrolled recipes left is less than 6 and we aren't fetching for more,
        // start loading more recipes
        if (diff < 6 && state.value != State.Loading) {
            loadComplexSearch(query)
        }
    }
}