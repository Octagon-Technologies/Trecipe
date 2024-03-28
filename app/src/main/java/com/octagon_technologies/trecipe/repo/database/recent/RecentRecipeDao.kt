package com.octagon_technologies.trecipe.repo.database.recent

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecentRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentRecipeEntity(recentRecipeEntity: RecentRecipeEntity)

    @Query("SELECT * FROM recentRecipeEntity")
    fun getAllRecentRecipe(): LiveData<List<RecentRecipeEntity>?>

    @Query("DELETE FROM recentRecipeEntity")
    suspend fun clearRecentRecipes()
}