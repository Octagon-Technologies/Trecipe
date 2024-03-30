package com.octagon_technologies.trecipe.repo.database.recent.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("recentAutoComplete")
data class RecentAutoComplete(
    @PrimaryKey
    val id: Int,
    val name: String,
    val dateAdded: Long = System.currentTimeMillis()
)
