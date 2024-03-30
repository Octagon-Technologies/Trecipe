package com.octagon_technologies.trecipe.repo.network

import com.octagon_technologies.trecipe.repo.network.models.complex_search.RemoteComplexSearch
import com.octagon_technologies.trecipe.repo.network.models.instructions.RecipeInstruction
import com.octagon_technologies.trecipe.repo.network.models.selected_recipe.RemoteSelectedRecipe
import com.octagon_technologies.trecipe.repo.network.models.nutrition_details.NutritionDetailsResponse
import com.octagon_technologies.trecipe.repo.network.models.random_recipes.Recipes
import com.octagon_technologies.trecipe.repo.network.models.similar_recipe.RemoteSimilarRecipe
import com.octagon_technologies.trecipe.repo.network.models.suggestions.RemoteAutoCompleteSuggestion
import com.octagon_technologies.trecipe.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


const val RECIPE_BASE_URL = ""
const val RECIPE_API_KEY = ""

// For base url: https://api.spoonacular.com/
// Api key: fe85a32ea0034f4f8577d9b3a5006011
interface RecipeApi {

    // https://api.spoonacular.com/recipes/716429/information?apiKey=fe85a32ea0034f4f8577d9b3a5006011&includeNutrition=false
    @GET("recipes/{recipeId}/information?apiKey=${Constants.recipe_api_key}&includeNutrition=true")
    suspend fun getSelectedRecipe(
        @Path("recipeId") recipeId: Int
    ): RemoteSelectedRecipe

    // https://api.spoonacular.com/recipes/716429/similar?apiKey=fe85a32ea0034f4f8577d9b3a5006011
    @GET("/recipes/{recipeId}/similar?apiKey=${Constants.recipe_api_key}&number=6")
    suspend fun getSimilarRecipes(
        @Path("recipeId") recipeId: Int
    ): List<RemoteSimilarRecipe>


    // https://api.spoonacular.com/recipes/autocomplete?apiKey=fe85a32ea0034f4f8577d9b3a5006011&number=10&query=be
    @GET("recipes/autocomplete?apiKey=${Constants.recipe_api_key}&number=10")
    suspend fun getRecipeAutocompleteFromQuery(
        @Query("query") query: String
    ): List<RemoteAutoCompleteSuggestion>



    // https://api.spoonacular.com/recipes/random?apiKey=fe85a32ea0034f4f8577d9b3a5006011&number=15
    @GET("recipes/random?apiKey=${Constants.recipe_api_key}")
    suspend fun getRandomRecipes(
        @Query("number") number: Int
    ): Recipes

    // https://api.spoonacular.com/recipes/324694/analyzedInstructions?apiKey=fe85a32ea0034f4f8577d9b3a5006011
    @GET("recipes/{recipeId}/analyzedInstructions?apiKey=${Constants.recipe_api_key}")
    suspend fun getRecipeInstructions(
        @Path("recipeId") recipeId: Int
    ): List<RecipeInstruction>

    // https://api.spoonacular.com/recipes/complexSearch?apiKey=fe85a32ea0034f4f8577d9b3a5006011&query=beef
    @GET("recipes/complexSearch?apiKey=${Constants.recipe_api_key}")
    suspend fun getComplexSearch(
        @Query("query") query: String,
        @Query("offset") offset: Int,
        @Query("number") number: Int = 14
    ): RemoteComplexSearch


    // https://api.spoonacular.com/recipes/1003464/nutritionWidget.json?apiKey=b5e15b5613cd40ae80875252702bc715
    @GET("recipes/{recipeId}/nutritionWidget.json?apiKey=${Constants.recipe_api_key}")
    suspend fun getNutritionDetails(
        @Path("recipeId") recipeId: Int
    ): NutritionDetailsResponse

}
