package com.octagontechnologies.trecipe.repo

import com.octagontechnologies.trecipe.domain.ErrorType
import com.octagontechnologies.trecipe.domain.Resource
import com.octagontechnologies.trecipe.domain.discover.DiscoverRecipe
import com.octagontechnologies.trecipe.domain.doOperation
import com.octagontechnologies.trecipe.repo.database.discover.daily.DailyLocalRecipe
import com.octagontechnologies.trecipe.repo.database.discover.daily.DailyRecipeDao
import com.octagontechnologies.trecipe.repo.database.discover.try_out.TryOutRecipe
import com.octagontechnologies.trecipe.repo.database.discover.try_out.TryOutRecipeDao
import com.octagontechnologies.trecipe.repo.dto.toDiscoverRecipe
import com.octagontechnologies.trecipe.repo.dto.toSimilarRecipe
import com.octagontechnologies.trecipe.repo.network.RecipeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RecipeRepo @Inject constructor(
    private val recipeApi: RecipeApi,
    private val dailyRecipeDao: DailyRecipeDao,
    private val tryOutRecipeDao: TryOutRecipeDao
) {

    /**
     * Fetches a list of random recipes from the API
     *
     * If the call is successful, the DB gets updated with the new data.
     * Regardless of call success, the Source of truth remains to be the DB since it it always
     * up-to-date
     */
    suspend fun getDiscoverRecipes(): Resource<List<DiscoverRecipe>> = withContext(Dispatchers.IO) {
        try {
            val dailyRecipes =
                recipeApi.getRandomRecipes(8).randomRecipes?.map { it.toDiscoverRecipe() }
            dailyRecipes?.let { dailyRecipeDao.insertData(DailyLocalRecipe(it)) }
        } catch (e: Exception) {
            Timber.e(e)
            val localDailyRecipes = dailyRecipeDao.getDailyRecipesOneShot()?.listOfDiscoverRecipe

            when (e) {
                is UnknownHostException -> return@withContext Resource.Error(ErrorType.NoNetworkError, localDailyRecipes)
                is SocketTimeoutException -> return@withContext Resource.Error(ErrorType.ApiError, localDailyRecipes)
                is HttpException -> return@withContext Resource.Error(ErrorType.ApiError, localDailyRecipes)
            }
        }

        val localDailyRecipes = dailyRecipeDao.getDailyRecipesOneShot()?.listOfDiscoverRecipe
        return@withContext if (localDailyRecipes.isNullOrEmpty())
            Resource.Error(ErrorType.Empty)
        else
            Resource.Success(localDailyRecipes)
    }

    /**
     * Fetches a list of random recipes from the API
     *
     * If the call is successful, the DB gets updated with the new data.
     * Regardless of call success, the Source of truth remains to be the DB since it it always
     * up-to-date
     */
    suspend fun getTryOutRecipes(): Resource<List<DiscoverRecipe>> = withContext(Dispatchers.IO) {
        try {
            val tryOutRecipes =
                recipeApi.getRandomRecipes(8).randomRecipes?.map { it.toDiscoverRecipe() }
            tryOutRecipes?.let { tryOutRecipeDao.insertData(TryOutRecipe(it)) }
        } catch (e: Exception) {
            Timber.e(e)
            val localTryOutRecipes = tryOutRecipeDao.getTryOutRecipes()?.listOfDiscoverRecipe

            when (e) {
                is UnknownHostException -> return@withContext Resource.Error(ErrorType.NoNetworkError, localTryOutRecipes)
                is SocketTimeoutException -> return@withContext Resource.Error(ErrorType.ApiError, localTryOutRecipes)
                is HttpException -> return@withContext Resource.Error(ErrorType.ApiError, localTryOutRecipes)
            }
        }

        val localTryOutRecipes = tryOutRecipeDao.getTryOutRecipes()?.listOfDiscoverRecipe
        return@withContext if (localTryOutRecipes.isNullOrEmpty())
            Resource.Error(ErrorType.Empty)
        else
            Resource.Success(localTryOutRecipes)
    }


    suspend fun getSimilarRecipes(initialRecipeId: Int) = doOperation {
        Resource.Success(recipeApi.getSimilarRecipes(initialRecipeId).map { it.toSimilarRecipe() })
    }
}