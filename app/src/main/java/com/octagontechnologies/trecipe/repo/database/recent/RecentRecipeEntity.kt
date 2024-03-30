package com.octagontechnologies.trecipe.repo.database.recent

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe

@Entity(tableName = "recentRecipeEntity")
data class RecentRecipeEntity(
    val simpleRecipe: SimpleRecipe,
    val dateAddedToDB: Long = System.currentTimeMillis(),

    @PrimaryKey
    val recipeId: Int = simpleRecipe.recipeId
)