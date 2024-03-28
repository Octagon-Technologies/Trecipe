package com.octagon_technologies.trecipe.repo.dto

import com.octagon_technologies.trecipe.domain.search.AutoComplete
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe
import com.octagon_technologies.trecipe.repo.database.recent.search.RecentAutoComplete
import com.octagon_technologies.trecipe.repo.network.models.complex_search.RemoteComplexSearch
import com.octagon_technologies.trecipe.repo.network.models.suggestions.RemoteAutoCompleteSuggestion

fun List<RemoteAutoCompleteSuggestion>.toAutoComplete() = map {
        AutoComplete(it.id, it.title)
    }

fun RemoteComplexSearch.toListOfSearchResults() = this.searchResults.map {
    SimpleRecipe(it.id, it.title, it.image)
}

fun AutoComplete.toLocalRecentAutoComplete() =
    RecentAutoComplete(id, name)