package com.octagon_technologies.trecipe.repo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.octagon_technologies.trecipe.repo.database.discover.daily.DailyLocalRecipe
import com.octagon_technologies.trecipe.repo.database.discover.daily.DailyRecipeDao
import com.octagon_technologies.trecipe.repo.database.discover.try_out.TryOutRecipe
import com.octagon_technologies.trecipe.repo.database.discover.try_out.TryOutRecipeDao
import com.octagon_technologies.trecipe.repo.database.liked.LikedRecipeDao
import com.octagon_technologies.trecipe.repo.database.liked.LikedRecipeEntity
import com.octagon_technologies.trecipe.repo.database.recent.RecentRecipeDao
import com.octagon_technologies.trecipe.repo.database.recent.RecentRecipeEntity
import com.octagon_technologies.trecipe.repo.database.recent.search.RecentAutoComplete
import com.octagon_technologies.trecipe.repo.database.recent.search.RecentAutoCompleteDao
import com.octagon_technologies.trecipe.repo.database.saved.SavedRecipeDao
import com.octagon_technologies.trecipe.repo.database.saved.SavedRecipeEntity

@Database(
    entities = [LikedRecipeEntity::class, RecentRecipeEntity::class, DailyLocalRecipe::class, TryOutRecipe::class, SavedRecipeEntity::class, RecentAutoComplete::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(RecipeTypeConverters::class)
abstract class RecipeDatabase : RoomDatabase() {

    abstract val likedRecipeDao: LikedRecipeDao
    abstract val recentRecipeDao: RecentRecipeDao
    abstract val savedRecipeDao: SavedRecipeDao
    abstract val dailyRecipeDao: DailyRecipeDao
    abstract val tryOutRecipeDao: TryOutRecipeDao
    abstract val recentAutoCompleteDao: RecentAutoCompleteDao

}