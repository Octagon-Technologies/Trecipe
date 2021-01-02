package com.octagon_technologies.foodie.repo.database.recent

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.foodie.models.BasicRoomRecipe

@Entity(tableName = "recentRecipeEntity")
data class RecentRecipeEntity (
    val basicRoomRecipe: BasicRoomRecipe,

    @PrimaryKey
    val recipeId: Int = basicRoomRecipe.id
)