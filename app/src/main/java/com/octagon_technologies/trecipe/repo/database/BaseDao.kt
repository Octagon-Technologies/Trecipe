package com.octagon_technologies.trecipe.repo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface BaseDao <T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: T)

}