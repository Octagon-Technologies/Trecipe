package com.octagontechnologies.trecipe.repo.database.saved

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.octagontechnologies.trecipe.domain.recipe.RecipeDetails
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe

@Entity(tableName = "savedRecipeEntity")
data class SavedRecipeEntity (
////     I believe there's a way I can extract simple recipe from recipe details but let's
////     have both right now coz we are still NOOBS :)
    @ColumnInfo("simple_recipe")
    val simpleRecipe: SimpleRecipe,

    val dateAddedToDB: Long = System.currentTimeMillis(),
//
//    @ColumnInfo("recipe_details")
//    val recipeDetails: RecipeDetails,

    @PrimaryKey
    val recipeId: Int = simpleRecipe.recipeId
)