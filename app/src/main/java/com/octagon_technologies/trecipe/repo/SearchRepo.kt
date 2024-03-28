package com.octagon_technologies.trecipe.repo

import com.octagon_technologies.trecipe.domain.search.AutoComplete
import com.octagon_technologies.trecipe.repo.database.recent.search.RecentAutoComplete
import com.octagon_technologies.trecipe.repo.database.recent.search.RecentAutoCompleteDao
import com.octagon_technologies.trecipe.repo.dto.toAutoComplete
import com.octagon_technologies.trecipe.repo.dto.toListOfSearchResults
import com.octagon_technologies.trecipe.repo.network.RecipeApi
import javax.inject.Inject

class SearchRepo @Inject constructor(
    private val recentAutoCompleteDao: RecentAutoCompleteDao,
    private val recipeApi: RecipeApi
) {

    suspend fun getRecipesBasedOnSearch(query: String, offset: Int) =
        recipeApi.getComplexSearch(query, offset).toListOfSearchResults()

    suspend fun getAutoCompleteSuggestions(query: String): List<AutoComplete> =
        recipeApi.getRecipeAutocompleteFromQuery(query).toAutoComplete()

    suspend fun insertAutoComplete(autoComplete: AutoComplete) {
        recentAutoCompleteDao.insertData(RecentAutoComplete(autoComplete.id, autoComplete.name))
    }

    suspend fun getRecentAutoComplete() =
        recentAutoCompleteDao.getListOfRecentAutoComplete().map { AutoComplete(it.id, it.name) }

}