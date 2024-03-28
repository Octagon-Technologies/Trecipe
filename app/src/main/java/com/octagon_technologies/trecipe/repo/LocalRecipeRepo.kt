package com.octagon_technologies.trecipe.repo

import androidx.lifecycle.map
import com.octagon_technologies.trecipe.domain.recipe.RecipeDetails
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe
import com.octagon_technologies.trecipe.repo.database.liked.LikedRecipeDao
import com.octagon_technologies.trecipe.repo.database.recent.RecentRecipeDao
import com.octagon_technologies.trecipe.repo.database.saved.SavedRecipeDao
import com.octagon_technologies.trecipe.repo.dto.toLikedRecipe
import com.octagon_technologies.trecipe.repo.dto.toRecentRecipe
import com.octagon_technologies.trecipe.repo.dto.toSavedRecipe
import timber.log.Timber
import javax.inject.Inject

class LocalRecipeRepo @Inject constructor(
    private val likedRecipeDao: LikedRecipeDao,
    private val recentRecipeDao: RecentRecipeDao,
    private val savedRecipeDao: SavedRecipeDao,
//    private val randomRecipeDao: RandomRecipesDao
) {

    val likedRecipes = likedRecipeDao.getAllLikedRecipes()
        .map { list -> list?.map { it.simpleRecipe } }
    val recentRecipes = recentRecipeDao.getAllRecentRecipe()
        .map { list -> list?.map { it.simpleRecipe } }
    val savedRecipes = savedRecipeDao.getSavedSimpleRecipes()
        .map { list -> list?.map { it.simpleRecipe } }

    val likedRecipeIds = likedRecipes.map { list -> list?.map { it.recipeId } }
    val savedRecipesIds = savedRecipes.map { list -> list?.map { it.recipeId } }

    fun isSaved(recipeId: Int): Boolean  {
        val isSaved = savedRecipesIds.value?.contains(recipeId) ?: false
        Timber.d("isSaved is $isSaved")
        Timber.d("recipeId is $recipeId while savedRecipesIds is ${savedRecipesIds.value}")
        return isSaved
    }

    suspend fun likeOrUnLikeRecipe(recipeDetails: RecipeDetails) {
        if (likedRecipeIds.value?.contains(recipeDetails.recipeId) == true)
            deleteLikedRecipe(recipeDetails)
        else
            insertLikedRecipe(recipeDetails)
    }

    suspend fun saveOrUnSaveRecipe(simpleRecipe: SimpleRecipe) {
        Timber.d("savedRecipesIds.value is ${savedRecipesIds.value}")

        val alreadySaved = savedRecipesIds.value?.contains(simpleRecipe.recipeId)
        Timber.d("alreadySaved is $alreadySaved")

        if (alreadySaved == true)
            deleteSavedRecipe(simpleRecipe.recipeId)
        else
            insertSavedRecipe(simpleRecipe)
    }

    private suspend fun insertLikedRecipe(recipeDetails: RecipeDetails) {
        likedRecipeDao.insertLikedRecipeEntity(recipeDetails.toLikedRecipe())
    }

    private suspend fun deleteLikedRecipe(recipeDetails: RecipeDetails) {
        likedRecipeDao.deleteLikedRecipe(recipeDetails.toLikedRecipe())
    }


    suspend fun insertRecentRecipe(recipeDetails: RecipeDetails) {
        recentRecipeDao.insertRecentRecipeEntity(recipeDetails.toRecentRecipe())
    }

    suspend fun clearRecentRecipes() { recentRecipeDao.clearRecentRecipes() }

    private suspend fun insertSavedRecipe(simpleRecipe: SimpleRecipe) {
        savedRecipeDao.insertData(simpleRecipe.toSavedRecipe())
        Timber.d("insertSavedRecipe called in LocalRecipeRepo with recipeDetails.recipeId as ${simpleRecipe.recipeId}")
    }

    suspend fun getSavedRecipes() = savedRecipeDao.getSavedRecipes()
    suspend fun updateSavedRecipes(simpleRecipe: List<SimpleRecipe>) {
        savedRecipeDao.updateSavedRecipes(simpleRecipe.map { it.toSavedRecipe() })
    }

    suspend fun deleteSavedRecipe(recipeId: Int) {
        savedRecipeDao.deleteSavedRecipe(recipeId)
        Timber.d("deleteSavedRecipe called in LocalRecipeRepo with recipeDetails.recipeId as $recipeId")
    }


//    suspend fun insertRandomRecipes(randomRecipes: List<RandomRecipe>?) {
//        val randomRecipesEntity = RandomRecipesEntity(randomRecipes ?: listOf())
//        randomRecipeDao.insertRandomRecipesEntity(randomRecipesEntity)
//    }
//    suspend fun getRandomRecipes() =
//        randomRecipeDao.getAllRandomRecipesEntity()?.randomRecipes
}