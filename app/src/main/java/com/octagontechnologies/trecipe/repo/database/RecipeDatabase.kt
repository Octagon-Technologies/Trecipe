package com.octagontechnologies.trecipe.repo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.octagontechnologies.trecipe.repo.database.discover.daily.DailyLocalRecipe
import com.octagontechnologies.trecipe.repo.database.discover.daily.DailyRecipeDao
import com.octagontechnologies.trecipe.repo.database.discover.try_out.TryOutRecipe
import com.octagontechnologies.trecipe.repo.database.discover.try_out.TryOutRecipeDao
import com.octagontechnologies.trecipe.repo.database.liked.LikedRecipeDao
import com.octagontechnologies.trecipe.repo.database.liked.LikedRecipeEntity
import com.octagontechnologies.trecipe.repo.database.recent.RecentRecipeDao
import com.octagontechnologies.trecipe.repo.database.recent.RecentRecipeEntity
import com.octagontechnologies.trecipe.repo.database.recent.search.RecentAutoComplete
import com.octagontechnologies.trecipe.repo.database.recent.search.RecentAutoCompleteDao
import com.octagontechnologies.trecipe.repo.database.saved.SavedRecipeDao
import com.octagontechnologies.trecipe.repo.database.saved.SavedRecipeEntity

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