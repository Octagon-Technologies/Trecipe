package com.octagon_technologies.trecipe.di

//import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.octagon_technologies.trecipe.repo.database.LocalRecipeRepo
import com.octagon_technologies.trecipe.repo.network.RecipeApi
import com.octagon_technologies.trecipe.repo.network.RemoteRecipeRepo
import com.octagon_technologies.trecipe.utils.Constants
import com.octagon_technologies.trecipe.utils.SelectedRecipeUtils
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
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun providesSelectedRecipeUtils(remoteRecipeRepo: RemoteRecipeRepo) =
        SelectedRecipeUtils(remoteRecipeRepo)

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
        .baseUrl(Constants.base_url)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun providesRemoteRecipeRepo(
        retrofit: Retrofit,
        localRecipeRepo: LocalRecipeRepo
    ): RemoteRecipeRepo {
        val recipeApi = retrofit.create(RecipeApi::class.java)
        return RemoteRecipeRepo(localRecipeRepo, recipeApi)
    }

}