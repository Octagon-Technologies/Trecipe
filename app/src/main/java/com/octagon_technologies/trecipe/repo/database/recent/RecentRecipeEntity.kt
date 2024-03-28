package com.octagon_technologies.trecipe.repo.database.recent

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe

@Entity(tableName = "recentRecipeEntity")
data class RecentRecipeEntity(
    val simpleRecipe: SimpleRecipe,

    @PrimaryKey
    val recipeId: Int = simpleRecipe.recipeId
)