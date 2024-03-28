package com.octagon_technologies.trecipe.repo.database.saved

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe
import com.octagon_technologies.trecipe.repo.database.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SavedRecipeDao: BaseDao<SavedRecipeEntity> {

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun updateSavedRecipes(savedRecipe: List<SavedRecipeEntity>)

    @Query("SELECT * FROM savedRecipeEntity")
    abstract fun getSavedSimpleRecipes(): Flow<List<SavedRecipeEntity>?>

    @Query("SELECT simple_recipe FROM savedRecipeEntity")
    abstract suspend fun getSavedRecipes(): List<SimpleRecipe>?

//    @Query("SELECT * FROM savedRecipeEntity WHERE recipeId = :recipeId")
//    abstract suspend fun getSavedRecipeDetails(recipeId: Int): SavedRecipeEntity?

    @Query("DELETE FROM savedRecipeEntity WHERE recipeId = :recipeId")
    abstract suspend fun deleteSavedRecipe(recipeId: Int)
}