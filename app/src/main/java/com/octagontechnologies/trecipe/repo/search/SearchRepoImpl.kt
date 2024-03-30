package com.octagontechnologies.trecipe.repo.search

import com.octagontechnologies.trecipe.domain.Resource
import com.octagontechnologies.trecipe.domain.doOperation
import com.octagontechnologies.trecipe.domain.search.AutoComplete
import com.octagontechnologies.trecipe.repo.database.recent.search.RecentAutoCompleteDao
import com.octagontechnologies.trecipe.repo.dto.toAutoComplete
import com.octagontechnologies.trecipe.repo.dto.toListOfSearchResults
import com.octagontechnologies.trecipe.repo.dto.toLocalRecentAutoComplete
import com.octagontechnologies.trecipe.repo.network.RecipeApi
import javax.inject.Inject

class SearchRepoImpl @Inject constructor(
    private val recentAutoCompleteDao: RecentAutoCompleteDao,
    private val recipeApi: RecipeApi
): SearchRepo {

    override suspend fun getRecipesBasedOnSearch(query: String, offset: Int) = doOperation {
        Resource.Success(recipeApi.getComplexSearch(query, offset).toListOfSearchResults())
    }

    override suspend fun getAutoCompleteSuggestions(query: String): Resource<List<AutoComplete>> = doOperation {
       Resource.Success(recipeApi.getRecipeAutocompleteFromQuery(query).toAutoComplete())
    }

    override suspend fun saveAutoCompleteToDBHistory(autoComplete: AutoComplete) {
        recentAutoCompleteDao.insertData(autoComplete.toLocalRecentAutoComplete())
    }

    override suspend fun getRecentAutoComplete() =
        Resource.Success(recentAutoCompleteDao.getListOfRecentAutoComplete().map { AutoComplete(it.id, it.name) })

}