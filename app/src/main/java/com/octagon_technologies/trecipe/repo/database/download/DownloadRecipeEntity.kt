package com.octagon_technologies.trecipe.repo.database.download

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.trecipe.repo.network.models.instructions.RecipeInstruction
import com.octagon_technologies.trecipe.repo.network.models.selected_recipe.SelectedRecipe

@Entity(tableName = "downloadRecipeEntity")
data class DownloadRecipeEntity (
    val selectedRecipe: SelectedRecipe,
    val recipeInstructions: List<RecipeInstruction>,

    @PrimaryKey
    val recipeId: Int = selectedRecipe.id ?: -1
)