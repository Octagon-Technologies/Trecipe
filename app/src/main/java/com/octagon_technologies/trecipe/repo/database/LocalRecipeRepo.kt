package com.octagon_technologies.trecipe.repo.database

import com.octagon_technologies.trecipe.models.BasicRoomRecipe
import com.octagon_technologies.trecipe.repo.database.download.DownloadRecipeEntity
import com.octagon_technologies.trecipe.repo.database.liked.LikedRecipeEntity
import com.octagon_technologies.trecipe.repo.database.random.RandomRecipesEntity
import com.octagon_technologies.trecipe.repo.database.recent.RecentRecipeEntity
import com.octagon_technologies.trecipe.repo.network.models.instructions.RecipeInstruction
import com.octagon_technologies.trecipe.repo.network.models.random_recipes.RandomRecipe
import com.octagon_technologies.trecipe.repo.network.models.selected_recipe.SelectedRecipe
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
    suspend fun deleteLikedRecipe(basicRoomRecipe: BasicRoomRecipe){
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
    suspend fun deleteRecentRecipe(basicRoomRecipe: BasicRoomRecipe){
        val recentRecipeEntity = RecentRecipeEntity(basicRoomRecipe)
        recentRecipeDao.deleteRecentRecipe(recentRecipeEntity)
    }
    suspend fun getAllRecentRecipes() =
        recentRecipeDao.getAllRecentRecipe().map { it.basicRoomRecipe }


    suspend fun insertDownloadRecipe(selectedRecipe: SelectedRecipe, recipeInstructions: List<RecipeInstruction>) {
        val downloadRecipeEntity = DownloadRecipeEntity(selectedRecipe, recipeInstructions)
        downloadRecipeDao.insertDownloadRecipeEntity(downloadRecipeEntity)
        Timber.d("downloadRecipe called in LocalRecipeRepo with downloadRecipeEntity.recipeId as ${downloadRecipeEntity.recipeId}")
    }
    suspend fun deleteDownloadRecipe(selectedRecipe: SelectedRecipe) {
        downloadRecipeDao.deleteDownloadRecipe(selectedRecipe.id ?: return)
    }
    suspend fun getAllDownloadRecipes() =
        downloadRecipeDao.getAllDownloadRecipes().map { it.selectedRecipe }
    suspend fun getLocalDownloadRecipeInstructions(recipeId: Int) =
        downloadRecipeDao.getLocalSelectedRecipe(recipeId)?.recipeInstructions



    suspend fun insertRandomRecipes(randomRecipes: List<RandomRecipe>?) {
        val randomRecipesEntity = RandomRecipesEntity(randomRecipes ?: listOf())
        randomRecipeDao.insertRandomRecipesEntity(randomRecipesEntity)
    }
    suspend fun getRandomRecipes() =
        randomRecipeDao.getAllRandomRecipesEntity()?.randomRecipes
}