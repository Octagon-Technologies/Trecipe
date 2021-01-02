package com.octagon_technologies.foodie.repo.database.recent

import androidx.room.*

@Dao
interface RecentRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentRecipeEntity(recentRecipeEntity: RecentRecipeEntity)

    @Query("SELECT * FROM recentRecipeEntity")
    suspend fun getAllRecentRecipe(): List<RecentRecipeEntity>

    @Delete
    suspend fun deleteRecentRecipe(recentRecipeEntity: RecentRecipeEntity)
}