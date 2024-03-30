package com.octagontechnologies.trecipe.di

import com.octagontechnologies.trecipe.repo.network.RecipeApi
import com.octagontechnologies.trecipe.repo.nutrition.NutritionRepo
import com.octagontechnologies.trecipe.repo.nutrition.NutritionRepoImpl
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