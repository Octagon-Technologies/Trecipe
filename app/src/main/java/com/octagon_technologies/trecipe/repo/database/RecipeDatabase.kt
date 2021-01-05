package com.octagon_technologies.trecipe.repo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.octagon_technologies.trecipe.repo.database.download.DownloadRecipeDao
import com.octagon_technologies.trecipe.repo.database.download.DownloadRecipeEntity
import com.octagon_technologies.trecipe.repo.database.liked.LikedRecipeDao
import com.octagon_technologies.trecipe.repo.database.liked.LikedRecipeEntity
import com.octagon_technologies.trecipe.repo.database.random.RandomRecipesDao
import com.octagon_technologies.trecipe.repo.database.random.RandomRecipesEntity
import com.octagon_technologies.trecipe.repo.database.recent.RecentRecipeDao
import com.octagon_technologies.trecipe.repo.database.recent.RecentRecipeEntity

@Database(
    entities = [LikedRecipeEntity::class, RecentRecipeEntity::class, RandomRecipesEntity::class, DownloadRecipeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipeTypeConverters::class)
abstract class RecipeDatabase : RoomDatabase() {

    abstract val likedRecipeDao: LikedRecipeDao
    abstract val recentRecipeDao: RecentRecipeDao
    abstract val downloadDao: DownloadRecipeDao
    abstract val randomRecipesDao: RandomRecipesDao

}