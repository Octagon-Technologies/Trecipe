package com.octagon_technologies.trecipe.repo.database.liked

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe

@Entity(tableName = "likedRecipeEntity")
data class LikedRecipeEntity (
    val simpleRecipe: SimpleRecipe,

    @PrimaryKey
    val recipeId: Int = simpleRecipe.recipeId
)