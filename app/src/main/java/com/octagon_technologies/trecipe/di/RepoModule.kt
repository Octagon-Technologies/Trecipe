package com.octagon_technologies.trecipe.di

import com.octagon_technologies.trecipe.repo.network.RecipeApi
import com.octagon_technologies.trecipe.repo.nutrition.NutritionRepo
import com.octagon_technologies.trecipe.repo.nutrition.NutritionRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepoModule {

    @Provides
    @Singleton
    fun providesNutritionRepo(recipeApi: RecipeApi): NutritionRepo =
        NutritionRepoImpl(recipeApi)

}