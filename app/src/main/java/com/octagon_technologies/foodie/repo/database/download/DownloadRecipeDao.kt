package com.octagon_technologies.foodie.repo.database.download

import androidx.room.*

@Dao
interface DownloadRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownloadRecipeEntity(downloadRecipeEntity: DownloadRecipeEntity)

    @Query("SELECT * FROM downloadRecipeEntity")
    suspend fun getAllDownloadRecipes(): List<DownloadRecipeEntity>

    @Delete
    suspend fun deleteDownloadRecipe(downloadRecipeEntity: DownloadRecipeEntity)
}