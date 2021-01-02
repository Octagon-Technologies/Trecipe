package com.octagon_technologies.foodie.di

import com.octagon_technologies.foodie.repo.database.LocalRecipeRepo
import com.octagon_technologies.foodie.repo.network.RecipeApiService
import com.octagon_technologies.foodie.repo.network.RemoteRecipeRepo
import com.octagon_technologies.foodie.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.base_url)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun providesRemoteRecipeRepo(retrofit: Retrofit, localRecipeRepo: LocalRecipeRepo): RemoteRecipeRepo {
        val recipeApi = retrofit.create(RecipeApiService::class.java)
        return RemoteRecipeRepo(localRecipeRepo, recipeApi)
    }

}