package com.octagon_technologies.trecipe.repo.database.recent.search

import androidx.room.Dao
import androidx.room.Query
import com.octagon_technologies.trecipe.repo.database.BaseDao

@Dao
abstract class RecentAutoCompleteDao: BaseDao<RecentAutoComplete> {

    @Query("SELECT * FROM recentAutoComplete ORDER BY dateAdded DESC")
    abstract suspend fun getListOfRecentAutoComplete(): List<RecentAutoComplete>

}