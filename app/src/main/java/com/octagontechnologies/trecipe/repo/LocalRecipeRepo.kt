package com.octagontechnologies.trecipe.repo

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.octagontechnologies.trecipe.domain.recipe.RecipeDetails
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe
import com.octagontechnologies.trecipe.repo.database.liked.LikedRecipeDao
import com.octagontechnologies.trecipe.repo.database.recent.RecentRecipeDao
import com.octagontechnologies.trecipe.repo.database.saved.SavedRecipeDao
import com.octagontechnologies.trecipe.repo.dto.toLikedRecipe
import com.octagontechnologies.trecipe.repo.dto.toRecentRecipe
import com.octagontechnologies.trecipe.repo.dto.toSavedRecipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import timber.log.Timber
import javax.inject.Inject


class LocalRecipeRepo @Inject constructor(
    private val likedRecipeDao: LikedRecipeDao,
    private val recentRecipeDao: RecentRecipeDao,
    private val savedRecipeDao: SavedRecipeDao
) {
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main) + job

    val likedRecipes = likedRecipeDao.getAllLikedRecipes()
        .map { list ->
            with(Dispatchers.IO) {
                val sortedList = list?.sortedByDescending { it.dateAddedToDB }
                sortedList?.map { it.simpleRecipe }
            }
        }
        .asLiveData()

    val recentRecipes = recentRecipeDao.getAllRecentRecipe()
        .map { list ->
            with(Dispatchers.IO) {
                val sortedList = list?.sortedByDescending { it.dateAddedToDB }
                sortedList?.map { it.simpleRecipe }
            }
        }
        .asLiveData()

    val savedRecipes = savedRecipeDao.getSavedSimpleRecipes()
        .onEach {
            Timber.d("onEach called: $it")
        }
        .map { list ->
            Timber.d("savedRecipes in map{} is $list")
            with(Dispatchers.IO) {
                val sortedList = list?.sortedByDescending { it.dateAddedToDB }
                sortedList?.map { it.simpleRecipe }
            }
        }
        .stateIn(coroutineScope, SharingStarted.Eagerly, listOf())


    val likedRecipeIds = likedRecipes.map { list -> list?.map { it.recipeId } }
    val savedRecipesIds = savedRecipes.map { list -> list?.map { it.recipeId } }
        .stateIn(coroutineScope, SharingStarted.Eagerly, listOf())

    fun isSaved(recipeId: Int): Boolean {
        val isSaved = savedRecipesIds.value?.contains(recipeId) ?: false
        Timber.d("isSaved is $isSaved")
        Timber.d("recipeId is $recipeId while savedRecipesIds is ${savedRecipesIds.value}")
        return isSaved
    }

    suspend fun likeOrUnLikeRecipe(recipeDetails: RecipeDetails) {
        if (likedRecipeIds.value?.contains(recipeDetails.recipeId) == true)
            removeFromLiked(recipeDetails.recipeId)
        else
            insertLikedRecipe(recipeDetails)
    }

    suspend fun saveOrUnSaveRecipe(simpleRecipe: SimpleRecipe) {
        val alreadySaved = savedRecipesIds.value?.contains(simpleRecipe.recipeId)
        Timber.d("savedRecipesIds in saveOrUnSaveRecipe() are ${savedRecipesIds.value}")

        if (alreadySaved == true)
            removeFromSaved(simpleRecipe.recipeId)
        else
            insertSavedRecipe(simpleRecipe)
    }

    private suspend fun insertLikedRecipe(recipeDetails: RecipeDetails) {
        likedRecipeDao.insertLikedRecipeEntity(recipeDetails.toLikedRecipe())
    }

    suspend fun removeFromLiked(recipeId: Int) {
        val rowsDeleted = likedRecipeDao.deleteLikedRecipe(recipeId)
        Timber.d("isDeleted is $rowsDeleted")
    }


    suspend fun insertRecentRecipe(recipeDetails: RecipeDetails) {
        recentRecipeDao.insertRecentRecipeEntity(recipeDetails.toRecentRecipe())
    }

    suspend fun removeFromRecent(recipeId: Int) {
        recentRecipeDao.removeFromRecent(recipeId)
    }

    private suspend fun insertSavedRecipe(simpleRecipe: SimpleRecipe) {
        savedRecipeDao.insertData(simpleRecipe.toSavedRecipe())
        Timber.d("insertSavedRecipe called in LocalRecipeRepo with recipeDetails.recipeId as ${simpleRecipe.recipeId}")
    }

    suspend fun getSavedRecipes() = savedRecipeDao.getSavedRecipes()
    suspend fun updateSavedRecipes(simpleRecipe: List<SimpleRecipe>) {
        savedRecipeDao.updateSavedRecipes(simpleRecipe.map { it.toSavedRecipe() })
    }

    suspend fun removeFromSaved(recipeId: Int) {
        savedRecipeDao.deleteSavedRecipe(recipeId)
        Timber.d("deleteSavedRecipe called in LocalRecipeRepo with recipeDetails.recipeId as $recipeId")
    }

    // TODO: Know where to call this function.... As of now, I just know it's needed somewhere
    fun onDestroy() {
        job.cancel()
        coroutineScope.cancel()
    }

//    suspend fun insertRandomRecipes(randomRecipes: List<RandomRecipe>?) {
//        val randomRecipesEntity = RandomRecipesEntity(randomRecipes ?: listOf())
//        randomRecipeDao.insertRandomRecipesEntity(randomRecipesEntity)
//    }
//    suspend fun getRandomRecipes() =
//        randomRecipeDao.getAllRandomRecipesEntity()?.randomRecipes
}