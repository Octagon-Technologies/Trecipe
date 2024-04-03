package com.octagontechnologies.trecipe.repo.network

import com.octagontechnologies.trecipe.repo.network.models.complex_search.RemoteComplexSearch
import com.octagontechnologies.trecipe.repo.network.models.instructions.RecipeInstruction
import com.octagontechnologies.trecipe.repo.network.models.nutrition_details.NutritionDetailsResponse
import com.octagontechnologies.trecipe.repo.network.models.random_recipes.Recipes
import com.octagontechnologies.trecipe.repo.network.models.selected_recipe.RemoteSelectedRecipe
import com.octagontechnologies.trecipe.repo.network.models.similar_recipe.RemoteSimilarRecipe
import com.octagontechnologies.trecipe.repo.network.models.suggestions.RemoteAutoCompleteSuggestion
import com.octagontechnologies.trecipe.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


const val RECIPE_BASE_URL = "https://api.spoonacular.com/"

// For base url: https://api.spoonacular.com/
// Api key: fe85a32ea0034f4f8577d9b3a5006011
interface RecipeApi {

    // https://api.spoonacular.com/recipes/716429/information?apiKey=fe85a32ea0034f4f8577d9b3a5006011&includeNutrition=false
    @GET("recipes/{recipeId}/information?includeNutrition=true")
    suspend fun getSelectedRecipe(
        @Path("recipeId") recipeId: Int,
        @Query("apiKey") apiKey: String = BuildConfig.RECIPE_API_KEY
    ): RemoteSelectedRecipe

    // https://api.spoonacular.com/recipes/716429/similar?apiKey=fe85a32ea0034f4f8577d9b3a5006011
    @GET("/recipes/{recipeId}/similar?number=6")
    suspend fun getSimilarRecipes(
        @Path("recipeId") recipeId: Int,
        @Query("apiKey") apiKey: String = BuildConfig.RECIPE_API_KEY
    ): List<RemoteSimilarRecipe>


    // https://api.spoonacular.com/recipes/autocomplete?apiKey=fe85a32ea0034f4f8577d9b3a5006011&number=10&query=be
    @GET("recipes/autocomplete?number=10")
    suspend fun getRecipeAutocompleteFromQuery(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String = BuildConfig.RECIPE_API_KEY
    ): List<RemoteAutoCompleteSuggestion>


    // https://api.spoonacular.com/recipes/random?apiKey=fe85a32ea0034f4f8577d9b3a5006011&number=15
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int,
        @Query("apiKey") apiKey: String = BuildConfig.RECIPE_API_KEY
    ): Recipes

    // https://api.spoonacular.com/recipes/324694/analyzedInstructions?apiKey=fe85a32ea0034f4f8577d9b3a5006011
    @GET("recipes/{recipeId}/analyzedInstructions")
    suspend fun getRecipeInstructions(
        @Path("recipeId") recipeId: Int,
        @Query("apiKey") apiKey: String = BuildConfig.RECIPE_API_KEY
    ): List<RecipeInstruction>

    // https://api.spoonacular.com/recipes/complexSearch?apiKey=fe85a32ea0034f4f8577d9b3a5006011&query=beef
    @GET("recipes/complexSearch")
    suspend fun getComplexSearch(
        @Query("query") query: String,
        @Query("offset") offset: Int,
        @Query("number") number: Int = 14,
        @Query("apiKey") apiKey: String = BuildConfig.RECIPE_API_KEY
    ): RemoteComplexSearch


    // https://api.spoonacular.com/recipes/1003464/nutritionWidget.json?apiKey=b5e15b5613cd40ae80875252702bc715
    @GET("recipes/{recipeId}/nutritionWidget.json")
    suspend fun getNutritionDetails(
        @Path("recipeId") recipeId: Int,
        @Query("apiKey") apiKey: String = BuildConfig.RECIPE_API_KEY
    ): NutritionDetailsResponse

}
