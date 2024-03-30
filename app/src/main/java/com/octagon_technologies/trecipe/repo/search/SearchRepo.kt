package com.octagon_technologies.trecipe.repo.search

import com.octagon_technologies.trecipe.domain.Resource
import com.octagon_technologies.trecipe.domain.search.AutoComplete
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe


interface SearchRepo {

    /**
     * Fetches recipes that match the selected search query
     *
     * @param query - Recipe search query to load
     * @param offset - Position reached in search results
     */
    suspend fun getRecipesBasedOnSearch(query: String, offset: Int): Resource<List<SimpleRecipe>>


    /**
     * Get autocomplete suggestions based on what the user has typed so far
     *
     * @param query - Query typed so far by the user
     */
    suspend fun getAutoCompleteSuggestions(query: String): Resource<List<AutoComplete>>

    /**
     * Inserts the selected autocomplete suggestion to the DB
     */
    suspend fun saveAutoCompleteToDBHistory(autoComplete: AutoComplete)

    /**
     * Gets a list of the previous autocomplete suggestions in the DB history
     */
    suspend fun getRecentAutoComplete(): Resource<List<AutoComplete>>
}