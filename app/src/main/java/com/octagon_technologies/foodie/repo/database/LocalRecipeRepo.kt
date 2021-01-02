package com.octagon_technologies.foodie.repo.database

import com.octagon_technologies.foodie.repo.database.download.DownloadRecipeEntity
import com.octagon_technologies.foodie.repo.database.liked.LikedRecipeEntity
import com.octagon_technologies.foodie.repo.database.random.RandomRecipesEntity
import com.octagon_technologies.foodie.repo.database.recent.RecentRecipeEntity
import com.octagon_technologies.foodie.repo.network.models.recipes.Recipe
import com.octagon_technologies.foodie.repo.network.models.selected_recipe.SelectedRecipe
import com.octagon_technologies.foodie.utils.toArrayList
import timber.log.Timber
import javax.inject.Inject

class LocalRecipeRepo @Inject constructor(recipeDatabase: RecipeDatabase) {

    private val likedRecipeDao = recipeDatabase.likedRecipeDao
    private val recentRecipeDao = recipeDatabase.recentRecipeDao
    private val downloadRecipeDao = recipeDatabase.downloadDao
    private val randomRecipeDao = recipeDatabase.randomRecipesDao
    
    suspend fun insertLikedRecipe(selectedRecipe: SelectedRecipe) {
        val basicRoomRecipe = selectedRecipe.toBasicRoomRecipe()
        val likedRecipeEntity = LikedRecipeEntity(basicRoomRecipe)
        likedRecipeDao.insertLikedRecipeEntity(likedRecipeEntity)
    }
    suspend fun deleteLikedRecipe(selectedRecipe: SelectedRecipe){
        val basicRoomRecipe = selectedRecipe.toBasicRoomRecipe()
        val likedRecipeEntity = LikedRecipeEntity(basicRoomRecipe)
        likedRecipeDao.deleteLikedRecipe(likedRecipeEntity)
    }
    suspend fun getAllLikedRecipes() =
        likedRecipeDao.getAllLikedRecipes().map { it.basicRoomRecipe }


    suspend fun insertRecentRecipe(selectedRecipe: SelectedRecipe) {
        val basicRoomRecipe = selectedRecipe.toBasicRoomRecipe()
        val recentRecipeEntity = RecentRecipeEntity(basicRoomRecipe)
        recentRecipeDao.insertRecentRecipeEntity(recentRecipeEntity)
    }
    suspend fun deleteRecentRecipe(selectedRecipe: SelectedRecipe){
        val basicRoomRecipe = selectedRecipe.toBasicRoomRecipe()
        val recentRecipeEntity = RecentRecipeEntity(basicRoomRecipe)
        recentRecipeDao.deleteRecentRecipe(recentRecipeEntity)
    }
    suspend fun getAllRecentRecipes() =
        recentRecipeDao.getAllRecentRecipe().map { it.basicRoomRecipe }


    suspend fun insertDownloadRecipe(selectedRecipe: SelectedRecipe) {
        val downloadRecipeEntity = DownloadRecipeEntity(selectedRecipe)
        downloadRecipeDao.insertDownloadRecipeEntity(downloadRecipeEntity)
        Timber.d("downloadRecipe called in LocalRecipeRepo with downloadRecipeEntity.recipeId as ${downloadRecipeEntity.recipeId}")
    }
    suspend fun deleteDownloadRecipe(selectedRecipe: SelectedRecipe){
        val downloadRecipeEntity = DownloadRecipeEntity(selectedRecipe)
        downloadRecipeDao.deleteDownloadRecipe(downloadRecipeEntity)
    }
    suspend fun getAllDownloadRecipes() =
        downloadRecipeDao.getAllDownloadRecipes().map { it.selectedRecipe }


    suspend fun insertRandomRecipes(randomRecipes: List<Recipe>?) {
        val randomRecipesEntity = RandomRecipesEntity(randomRecipes ?: listOf())
        randomRecipeDao.insertRandomRecipesEntity(randomRecipesEntity)
    }
    suspend fun getRandomRecipes() =
        randomRecipeDao.getAllRandomRecipesEntity().randomRecipes.toArrayList()
}