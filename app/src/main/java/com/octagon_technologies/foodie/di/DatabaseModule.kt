package com.octagon_technologies.foodie.di

import android.content.Context
import androidx.room.Room
import com.octagon_technologies.foodie.repo.database.LocalRecipeRepo
import com.octagon_technologies.foodie.repo.database.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun providesRecipeDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RecipeDatabase::class.java, "RecipeDatabase")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providesLocalRecipeRepo(recipeDatabase: RecipeDatabase) =
        LocalRecipeRepo(recipeDatabase)
}