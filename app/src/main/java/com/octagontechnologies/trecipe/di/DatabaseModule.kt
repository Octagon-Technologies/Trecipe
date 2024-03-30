package com.octagontechnologies.trecipe.di

import android.content.Context
import androidx.room.Room
import com.octagontechnologies.trecipe.repo.database.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun providesRecipeDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RecipeDatabase::class.java, "RecipeDatabase")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providesLikedDao(recipeDatabase: RecipeDatabase) = recipeDatabase.likedRecipeDao
    @Provides
    fun providesRecentDao(recipeDatabase: RecipeDatabase) = recipeDatabase.recentRecipeDao
    @Provides
    fun providesSavedDao(recipeDatabase: RecipeDatabase) = recipeDatabase.savedRecipeDao
    @Provides
    fun providesDailyRecipeDao(recipeDatabase: RecipeDatabase) = recipeDatabase.dailyRecipeDao

    @Provides
    fun providesTryOutDao(recipeDatabase: RecipeDatabase) = recipeDatabase.tryOutRecipeDao

    @Provides
    fun providesRecentAutoCompleteRecipeDao(recipeDatabase: RecipeDatabase) = recipeDatabase.recentAutoCompleteDao
}