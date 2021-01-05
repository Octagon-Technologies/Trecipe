package com.octagon_technologies.trecipe.repo.network

import androidx.lifecycle.MutableLiveData
import com.octagon_technologies.trecipe.models.State
import com.octagon_technologies.trecipe.repo.database.LocalRecipeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject


class RemoteRecipeRepo @Inject constructor(
//    @ApplicationContext private val context: Context,
//    private val moshi: Moshi,
    private val localRecipeRepo: LocalRecipeRepo,
    private val retrofitApiService: RecipeApiService,
) {
//    val randomRecipesJsonAdapter: JsonAdapter<Recipes> = moshi.adapter(Recipes::class.java)
//
//    if (BuildConfig.DEBUG) {
//        val identifier = context
//            .resources
//            .getIdentifier("random_recipe", "raw", context.packageName)
//
//        val inputStream: InputStream = context.resources.openRawResource(identifier)
//        val randomRecipesJson =
//    } else {

    suspend fun getRandomRecipes(_state: MutableLiveData<State>) =
        flow {
            val randomRecipes = retrofitApiService.getRandomRecipes().randomRecipes
            localRecipeRepo.insertRandomRecipes(randomRecipes)
            emit(randomRecipes)
        }.catch { e ->
            localRecipeRepo.getRandomRecipes()?.also {
                emit(it)
            } ?: run {
                catchNoNetworkError(e, _state)
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getRecipeSuggestionsFromQuery(query: String, _state: MutableLiveData<State>) =
        flow {
            emit(retrofitApiService.getRecipeSuggestionsFromQuery(query))
        }.catch { e ->
            catchNoNetworkError(e, _state)
        }.flowOn(Dispatchers.IO)

    suspend fun getComplexSearch(query: String, offset: Int, _state: MutableLiveData<State>) =
        flow {
            emit(retrofitApiService.getComplexSearch(query, offset))
        }.catch { e ->
            catchNoNetworkError(e, _state)
        }.flowOn(Dispatchers.IO)


    suspend fun getSelectedRecipe(recipeId: Int, _state: MutableLiveData<State>) =
        flow {
            val selectedRecipe = retrofitApiService.getSelectedRecipe(recipeId)
            emit(selectedRecipe)
        }.catch { e ->
            catchNoNetworkError(e, _state)
        }.flowOn(Dispatchers.IO)

    suspend fun getRecipeInstructions(recipeId: Int, _state: MutableLiveData<State>) =
        flow {
            val recipeInstructions = retrofitApiService.getRecipeInstructions(recipeId)
            emit(recipeInstructions)
        }.catch { e ->
            localRecipeRepo.getLocalDownloadRecipeInstructions(recipeId)?.also {
                emit(it)
            } ?: run {
                catchNoNetworkError(e, _state)
            }
        }.flowOn(Dispatchers.IO)

    private suspend fun catchNoNetworkError(throwable: Throwable, _state: MutableLiveData<State>) {
        withContext(Dispatchers.Main) {
            when (throwable) {
                is HttpException -> {
                    Timber.i(throwable)
                    _state.value = State.ApiError
                }
                is UnknownHostException -> {
                    _state.value = State.NoNetworkError
                }
                else -> {
                    Timber.e(throwable)
                }
            }
        }
    }


}