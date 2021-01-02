package com.octagon_technologies.foodie.repo.network

import com.octagon_technologies.foodie.repo.network.models.instructions.RecipeInstruction
import com.octagon_technologies.foodie.repo.network.models.recipes.Recipes
import com.octagon_technologies.foodie.repo.network.models.selected_recipe.SelectedRecipe
import com.octagon_technologies.foodie.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path

// For base url: https://api.spoonacular.com/
// Api key: fe85a32ea0034f4f8577d9b3a5006011
interface RecipeApiService {

    // https://api.spoonacular.com/recipes/716429/information?apiKey=fe85a32ea0034f4f8577d9b3a5006011&includeNutrition=false
    @GET("recipes/{recipeId}/information?apiKey=${Constants.recipe_api_key}&includeNutrition=true")
    suspend fun getSelectedRecipe(
        @Path("recipeId") recipeId: Int
    ): SelectedRecipe

    // https://api.spoonacular.com/recipes/random?apiKey=fe85a32ea0034f4f8577d9b3a5006011&number=15
    @GET("recipes/random?apiKey=${Constants.recipe_api_key}&number=15")
    suspend fun getRandomRecipes(): Recipes

    // https://api.spoonacular.com/recipes/324694/analyzedInstructions?apiKey=fe85a32ea0034f4f8577d9b3a5006011
    @GET("recipes/{recipeId}/analyzedInstructions?apiKey=${Constants.recipe_api_key}")
    suspend fun getRecipeInstructions(
        @Path("recipeId") recipeId: Int
    ): List<RecipeInstruction>
}
