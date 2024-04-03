package com.octagontechnologies.trecipe.di

import com.octagontechnologies.trecipe.repo.database.recent.search.RecentAutoCompleteDao
import com.octagontechnologies.trecipe.repo.network.RECIPE_BASE_URL
import com.octagontechnologies.trecipe.repo.network.RecipeApi
import com.octagontechnologies.trecipe.repo.search.SearchRepo
import com.octagontechnologies.trecipe.repo.search.SearchRepoImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun providesOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(RECIPE_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()


    @Provides
    fun providesRecipeApi(retrofit: Retrofit) =
        retrofit.create<RecipeApi>()

    @Provides
    fun providesSearchRepo(
        recentAutoCompleteDao: RecentAutoCompleteDao,
        recipeApi: RecipeApi
    ): SearchRepo =
        SearchRepoImpl(recentAutoCompleteDao, recipeApi)

}