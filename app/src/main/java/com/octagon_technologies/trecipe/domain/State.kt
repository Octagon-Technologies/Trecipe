package com.octagon_technologies.trecipe.domain

import com.octagon_technologies.trecipe.R

enum class State {
    Empty,
    Loading,
    ApiError,
    NoNetworkError,
    Done;
}

enum class ErrorType {
    Empty,
    ApiError,
    NoNetworkError,
}

fun State.getMessageRes(): Int? =
    when (this) {
        State.ApiError -> R.string.api_limit_exceeded
        State.NoNetworkError -> R.string.no_network_available
        State.Empty -> R.string.no_recipes_found_for_search_query
        else -> null
    }