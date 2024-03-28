package com.octagon_technologies.trecipe.domain.search

// To be used in SearchResult and saving Liked, Saved and Recent
data class SimpleRecipe(
    val recipeId: Int,
    val recipeName: String,
    val recipeImage: String?
)
