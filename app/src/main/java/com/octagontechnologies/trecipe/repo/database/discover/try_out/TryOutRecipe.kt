package com.octagontechnologies.trecipe.repo.database.discover.try_out

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagontechnologies.trecipe.domain.discover.DiscoverRecipe

@Entity("tryOutRecipe")
data class TryOutRecipe(
    val listOfDiscoverRecipe: List<DiscoverRecipe>,

    @PrimaryKey(autoGenerate = false)
    val id: Int = 0
)
