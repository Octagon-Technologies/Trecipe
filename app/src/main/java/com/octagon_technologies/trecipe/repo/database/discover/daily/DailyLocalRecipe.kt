package com.octagon_technologies.trecipe.repo.database.discover.daily

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.trecipe.domain.discover.DiscoverRecipe

@Entity("dailyLocalRecipe")
data class DailyLocalRecipe(
    val listOfDiscoverRecipe: List<DiscoverRecipe>,

    @PrimaryKey(autoGenerate = false)
    val id: Int = 0
)
