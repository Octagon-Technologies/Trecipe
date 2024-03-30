package com.octagontechnologies.trecipe.repo.database.liked

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe

@Entity(tableName = "likedRecipeEntity")
data class LikedRecipeEntity (
    val simpleRecipe: SimpleRecipe,
    val dateAddedToDB: Long = System.currentTimeMillis(),

    @PrimaryKey
    val recipeId: Int = simpleRecipe.recipeId
)