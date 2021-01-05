package com.octagon_technologies.trecipe.repo.database.random

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.trecipe.repo.network.models.random_recipes.RandomRecipe

@Entity(tableName = "randomRecipesEntity")
data class RandomRecipesEntity(
    val randomRecipes: List<RandomRecipe>,

    @PrimaryKey
    val id: Int = 1,
)