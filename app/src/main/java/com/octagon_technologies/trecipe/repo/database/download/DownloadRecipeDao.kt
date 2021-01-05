package com.octagon_technologies.trecipe.repo.database.download

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.octagon_technologies.trecipe.repo.network.models.instructions.RecipeInstruction

@Dao
interface DownloadRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownloadRecipeEntity(downloadRecipeEntity: DownloadRecipeEntity)

    @Query("SELECT * FROM downloadRecipeEntity")
    suspend fun getAllDownloadRecipes(): List<DownloadRecipeEntity>

    @Query("SELECT recipeInstructions FROM downloadRecipeEntity WHERE recipeId = :recipeId")
    suspend fun getLocalSelectedRecipe(recipeId: Int): List<RecipeInstruction>?

    @Query("DELETE FROM downloadRecipeEntity WHERE recipeId = :recipeId")
    suspend fun deleteDownloadRecipe(recipeId: Int)
}