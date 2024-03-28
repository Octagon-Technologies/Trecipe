package com.octagon_technologies.trecipe.repo.database.recent

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentRecipeEntity(recentRecipeEntity: RecentRecipeEntity)

    @Query("SELECT * FROM recentRecipeEntity")
    fun getAllRecentRecipe(): Flow<List<RecentRecipeEntity>?>

    @Query("DELETE FROM recentRecipeEntity WHERE recipeId = :recipeId")
    suspend fun removeFromRecent(recipeId: Int)
}