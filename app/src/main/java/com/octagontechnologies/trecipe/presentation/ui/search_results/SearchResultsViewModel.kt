package com.octagontechnologies.trecipe.presentation.ui.search_results

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octagontechnologies.trecipe.domain.Resource
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe
import com.octagontechnologies.trecipe.repo.LocalRecipeRepo
import com.octagontechnologies.trecipe.repo.search.SearchRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchRepo: SearchRepo,
    private val localRecipeRepo: LocalRecipeRepo
) : ViewModel() {
    // Helps us know how many recipes have been fetched
    private var offset = 0

    val query = SearchResultsFragmentArgs.fromSavedStateHandle(savedStateHandle).query

    private val _listOfSearchResult =
        MutableLiveData<Resource<List<SimpleRecipe>>>(Resource.Loading())
    val listOfSearchResult: LiveData<Resource<List<SimpleRecipe>>> =
        _listOfSearchResult

    val savedRecipes = localRecipeRepo.savedRecipes

//    val listOfSearchResult = MediatorLiveData<Resource<List<SimpleRecipe>>>().apply {
//        val savedRecipes = localRecipeRepo.savedRecipesIds.value ?: listOf()
//
//
//        addSource(localRecipeRepo.savedRecipesIds) {
//            val result =  _listOfSearchResult.value?.data ?: return@addSource
//            value = applySavesToRecipes(result, savedRecipes)
//        }
//
//        /**
//         * If the result.data == null, it means:
//         * (1) The recipe fetch state is LOADING
//         * (2) The load operation failed with a Resource.Error() type of result
//         */
//        addSource(_listOfSearchResult) { result ->
//            if (result.data == null) return@addSource
//            value = applySavesToRecipes(result.data, savedRecipes)
//        }
//    }.distinctUntilChanged()

    private var isLoadingMore = false

    fun loadComplexSearch() {
        viewModelScope.launch {
            _listOfSearchResult.value = searchRepo.getRecipesBasedOnSearch(query, offset)
            offset += 10
        }
    }

//    /**
//     * Whenever we receive a list of search results, we check the DB list of saved recipe ids.
//     * We then edit the SimpleRecipe.isSaved property of the recipes that are in our saved list
//     */
//    private fun applySavesToRecipes(recipes: List<SimpleRecipe>, likedRecipes: List<Int>?) =
//        Resource.Success(
//            recipes.map {
//                if (likedRecipes?.contains(it.recipeId) == true)
//                    it.apply { it.isSaved = true }
//                else
//                    it
//            }
//        )


    fun saveOrUnsaveRecipe(simpleRecipe: SimpleRecipe) = viewModelScope.launch {
        localRecipeRepo.saveOrUnSaveRecipe(simpleRecipe)
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