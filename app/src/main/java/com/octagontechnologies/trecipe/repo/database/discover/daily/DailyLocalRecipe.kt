package com.octagontechnologies.trecipe.repo.database.discover.daily

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagontechnologies.trecipe.domain.discover.DiscoverRecipe

@Entity("dailyLocalRecipe")
data class DailyLocalRecipe(
    val listOfDiscoverRecipe: List<DiscoverRecipe>,

    @PrimaryKey(autoGenerate = false)
    val id: Int = 0
)
