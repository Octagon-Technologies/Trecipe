package com.octagontechnologies.trecipe.repo.dto

import com.octagontechnologies.trecipe.domain.search.AutoComplete
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe
import com.octagontechnologies.trecipe.repo.database.recent.search.RecentAutoComplete
import com.octagontechnologies.trecipe.repo.network.models.complex_search.RemoteComplexSearch
import com.octagontechnologies.trecipe.repo.network.models.suggestions.RemoteAutoCompleteSuggestion

fun List<RemoteAutoCompleteSuggestion>.toAutoComplete() = map {
        AutoComplete(it.id, it.title)
    }

fun RemoteComplexSearch.toListOfSearchResults() = this.searchResults.map {
    SimpleRecipe(it.id, it.title, it.image)
}

fun AutoComplete.toLocalRecentAutoComplete() =
    RecentAutoComplete(id, name)