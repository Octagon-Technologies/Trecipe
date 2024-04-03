package com.octagontechnologies.trecipe.domain.search

// To be used in SearchResult and saving Liked, Saved and Recent
data class SimpleRecipe(
    val recipeId: Int,
    val recipeName: String,
    val recipeImage: String?,

//
//    /**
//     * Small trick for the Search Results Fragment
//     */
//    var isSaved: Boolean = false
)
