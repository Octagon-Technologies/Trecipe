package com.octagon_technologies.foodie.repo.network

import com.octagon_technologies.foodie.repo.database.LocalRecipeRepo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject


class RemoteRecipeRepo @Inject constructor(
//    @ApplicationContext private val context: Context,
//    private val moshi: Moshi,
    private val localRecipeRepo: LocalRecipeRepo,
    private val retrofitApiService: RecipeApiService
) {
//    val randomRecipesJsonAdapter: JsonAdapter<Recipes> = moshi.adapter(Recipes::class.java)
//
//    if (BuildConfig.DEBUG) {
//        val identifier = context
//            .resources
//            .getIdentifier("random_recipe", "raw", context.packageName)
//
//        val inputStream: InputStream = context.resources.openRawResource(identifier).readBytes()
//        val randomRecipesJson =
//    } else {

    suspend fun getRandomRecipes() =
        flow {
            val randomRecipes = retrofitApiService.getRandomRecipes()
            localRecipeRepo.insertRandomRecipes(randomRecipes.recipes)
            emit(randomRecipes)
        }.catch { e ->
            catchNoNetworkError(e)
        }


    suspend fun getSelectedRecipe(recipeId: Int) =
        flow {
            val selectedRecipe = retrofitApiService.getSelectedRecipe(recipeId)
            emit(selectedRecipe)
        }.catch { e ->
            catchNoNetworkError(e)
        }

    suspend fun getRecipeInstructions(recipeId: Int) =
        flow {
            val recipeInstructions = retrofitApiService.getRecipeInstructions(recipeId)
            emit(recipeInstructions)
        }.catch { e ->
            catchNoNetworkError(e)
        }

    private fun catchNoNetworkError(throwable: Throwable) {
        if (throwable is HttpException || throwable is UnknownHostException) {
            Timber.i(throwable)
        } else {
            Timber.e(throwable)
        }
    }
}