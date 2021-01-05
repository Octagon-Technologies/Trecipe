package com.octagon_technologies.trecipe.repo.database.random

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RandomRecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRandomRecipesEntity(randomRecipesEntity: RandomRecipesEntity)

    @Query("SELECT * FROM randomRecipesEntity")
    suspend fun getAllRandomRecipesEntity(): RandomRecipesEntity?
}