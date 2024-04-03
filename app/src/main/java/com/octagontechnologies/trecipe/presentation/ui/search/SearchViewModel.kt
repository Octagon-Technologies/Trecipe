package com.octagontechnologies.trecipe.presentation.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.trecipe.domain.Resource
import com.octagontechnologies.trecipe.domain.search.AutoComplete
import com.octagontechnologies.trecipe.repo.search.SearchRepo
import com.octagontechnologies.trecipe.repo.search.SearchRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepo: SearchRepo
) : ViewModel() {

    private val _listOfRecentAutoComplete = MutableLiveData<Resource<List<AutoComplete>>>(Resource.Loading())
    val listOfRecentAutoComplete: LiveData<Resource<List<AutoComplete>>> = _listOfRecentAutoComplete

    private val _recipeSuggestions = MutableLiveData<Resource<List<AutoComplete>>>(Resource.Loading())
    val recipeSuggestions: LiveData<Resource<List<AutoComplete>>> = _recipeSuggestions

    private val _navigateToSearchResults = MutableLiveData<String?>()
    val navigateToSearchResults: LiveData<String?> = _navigateToSearchResults

    init {
        viewModelScope.launch {
            _listOfRecentAutoComplete.value = searchRepo.getRecentAutoComplete()
        }
    }

    fun loadSuggestions(query: String) {
        viewModelScope.launch {
            _recipeSuggestions.value = searchRepo.getAutoCompleteSuggestions(query)
        }
    }

    fun addQueryToRecent(autoComplete: AutoComplete) {
        viewModelScope.launch {
            searchRepo.saveAutoCompleteToDBHistory(autoComplete)
            _navigateToSearchResults.value = autoComplete.name
        }
    }

    fun resetNavigateToSearchResults() {
        _navigateToSearchResults.value = null
    }
}