package com.octagon_technologies.foodie.repo.database.liked

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.foodie.models.BasicRoomRecipe

@Entity(tableName = "likedRecipeEntity")
data class LikedRecipeEntity (
    val basicRoomRecipe: BasicRoomRecipe,

    @PrimaryKey
    val recipeId: Int = basicRoomRecipe.id
)