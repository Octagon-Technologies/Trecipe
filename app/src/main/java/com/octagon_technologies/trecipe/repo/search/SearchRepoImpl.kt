package com.octagon_technologies.trecipe.repo.search

import com.octagon_technologies.trecipe.domain.Resource
import com.octagon_technologies.trecipe.domain.doOperation
import com.octagon_technologies.trecipe.domain.search.AutoComplete
import com.octagon_technologies.trecipe.repo.database.recent.search.RecentAutoCompleteDao
import com.octagon_technologies.trecipe.repo.dto.toAutoComplete
import com.octagon_technologies.trecipe.repo.dto.toListOfSearchResults
import com.octagon_technologies.trecipe.repo.dto.toLocalRecentAutoComplete
import com.octagon_technologies.trecipe.repo.network.RecipeApi
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