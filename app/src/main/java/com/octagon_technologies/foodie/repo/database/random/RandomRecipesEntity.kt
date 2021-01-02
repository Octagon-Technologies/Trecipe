package com.octagon_technologies.foodie.repo.database.random

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.foodie.repo.network.models.recipes.Recipe

@Entity(tableName = "randomRecipesEntity")
data class RandomRecipesEntity(
    val randomRecipes: List<Recipe>,

    @PrimaryKey
    val id: Int = 1,
)