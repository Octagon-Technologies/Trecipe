package com.octagon_technologies.trecipe.repo.database.saved

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagon_technologies.trecipe.domain.recipe.RecipeDetails
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe

@Entity(tableName = "savedRecipeEntity")
data class SavedRecipeEntity (
////     I believe there's a way I can extract simple recipe from recipe details but let's
////     have both right now coz we are still NOOBS :)
    @ColumnInfo("simple_recipe")
    val simpleRecipe: SimpleRecipe,
//
//    @ColumnInfo("recipe_details")
//    val recipeDetails: RecipeDetails,

    @PrimaryKey
    val recipeId: Int = simpleRecipe.recipeId
)