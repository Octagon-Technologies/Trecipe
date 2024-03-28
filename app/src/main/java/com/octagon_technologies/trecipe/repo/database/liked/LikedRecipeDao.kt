package com.octagon_technologies.trecipe.repo.database.liked

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikedRecipeEntity(likedRecipeEntity: LikedRecipeEntity)

    @Query("SELECT * FROM likedRecipeEntity")
    fun getAllLikedRecipes(): Flow<List<LikedRecipeEntity>?>

    // Replace this with an id Delete
    @Query("DELETE FROM likedRecipeEntity WHERE recipeId = :recipeId")
    suspend fun deleteLikedRecipe(recipeId: Int): Int
}