package com.octagon_technologies.trecipe.repo.database.liked

import androidx.room.*

@Dao
interface LikedRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikedRecipeEntity(likedRecipeEntity: LikedRecipeEntity)

    @Query("SELECT * FROM likedRecipeEntity")
    suspend fun getAllLikedRecipes(): List<LikedRecipeEntity>

    @Delete
    suspend fun deleteLikedRecipe(likedRecipeEntity: LikedRecipeEntity)
}