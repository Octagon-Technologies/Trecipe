package com.octagontechnologies.trecipe.presentation.ui.search_results

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.trecipe.domain.Resource
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe
import com.octagontechnologies.trecipe.repo.search.SearchRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchRepo: SearchRepoImpl
) : ViewModel() {
    // Helps us know how many recipes have been fetched
    private var offset = 0

    val query = SearchResultsFragmentArgs.fromSavedStateHandle(savedStateHandle).query

    private val _listOfSearchResult =
        MutableLiveData<Resource<List<SimpleRecipe>>>(Resource.Loading())
    val listOfSearchResult: LiveData<Resource<List<SimpleRecipe>>> = _listOfSearchResult

    private var isLoadingMore = false

    fun loadComplexSearch() {
        viewModelScope.launch {
            _listOfSearchResult.value = searchRepo.getRecipesBasedOnSearch(query, offset)
            offset += 10
        }
    }

    fun checkIfShouldReloadMore(lastItemPosition: Int) {
        // If the list is empty or null, the diff will be 10 hence no data fetched
        val searchResultsSize = listOfSearchResult.value?.data?.size ?: 14
        val diff = searchResultsSize - lastItemPosition

        // If the number of unscrolled recipes left is less than 6 and we aren't fetching for more,
        // start loading more recipes
        if (diff < 8 && !isLoadingMore) {
            isLoadingMore = true
            loadComplexSearch()
            isLoadingMore = false
        }
    }
}