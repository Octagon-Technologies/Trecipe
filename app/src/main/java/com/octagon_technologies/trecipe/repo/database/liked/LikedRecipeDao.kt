package com.octagon_technologies.trecipe.repo.database.liked

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LikedRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLikedRecipeEntity(likedRecipeEntity: LikedRecipeEntity)

    @Query("SELECT * FROM likedRecipeEntity")
    fun getAllLikedRecipes(): LiveData<List<LikedRecipeEntity>?>

    // Replace this with an id Delete
    @Delete
    suspend fun deleteLikedRecipe(likedRecipeEntity: LikedRecipeEntity)
}