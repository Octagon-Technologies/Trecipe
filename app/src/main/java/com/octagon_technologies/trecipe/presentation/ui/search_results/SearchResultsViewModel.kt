package com.octagon_technologies.trecipe.presentation.ui.search_results

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagon_technologies.trecipe.models.State
import com.octagon_technologies.trecipe.repo.network.RemoteRecipeRepo
import com.octagon_technologies.trecipe.repo.network.models.complex_search.ComplexSearch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchResultsViewModel @ViewModelInject constructor(
    private val remoteRecipeRepo: RemoteRecipeRepo,
) : ViewModel() {
    private var offset = 0
    private var totalCount = 10

    private val _state = MutableLiveData(State.Loading)
    val state: LiveData<State> = _state

    private val _complexSearch = MutableLiveData<ComplexSearch>()
    val complexSearch: LiveData<ComplexSearch> = _complexSearch

    fun loadComplexSearch(query: String) {
        viewModelScope.launch {
            if (offset < totalCount) {
                remoteRecipeRepo
                    .getComplexSearch(query, offset, _state)
                    .collect {
                        _complexSearch.value = it
                        offset += 10
                        totalCount = it.totalResults ?: offset

                        if (_state.value != State.Done)
                            _state.value = State.Done
                    }
            }
        }
    }

}