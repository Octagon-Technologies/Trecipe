package com.octagon_technologies.foodie.repo.database.download

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.foodie.repo.network.models.selected_recipe.SelectedRecipe

@Entity(tableName = "downloadRecipeEntity")
data class DownloadRecipeEntity (
    val selectedRecipe: SelectedRecipe,

    @PrimaryKey
    val recipeId: Int = selectedRecipe.id ?: -1
)