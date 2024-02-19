package com.octagon_technologies.trecipe.repo.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.octagon_technologies.trecipe.models.BasicRoomRecipe
import com.octagon_technologies.trecipe.repo.network.models.instructions.RecipeInstruction
import com.octagon_technologies.trecipe.repo.network.models.random_recipes.RandomRecipe
import com.octagon_technologies.trecipe.repo.network.models.selected_recipe.SelectedRecipe
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@TypeConverters
class RecipeTypeConverters {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val basicRoomJsonAdapter: JsonAdapter<BasicRoomRecipe> =
        moshi.adapter(BasicRoomRecipe::class.java)

    private val selectedRecipeJsonAdapter: JsonAdapter<SelectedRecipe> =
        moshi.adapter(SelectedRecipe::class.java)

    private val randomRandomRecipeJsonAdapter: JsonAdapter<List<RandomRecipe>> =
        moshi.adapter(Types.newParameterizedType(List::class.java, RandomRecipe::class.java))

    private val listOfRecipeInstructionJsonAdapter: JsonAdapter<List<RecipeInstruction>> =
        moshi.adapter(Types.newParameterizedType(List::class.java, RecipeInstruction::class.java))

    private val recipeInstructionJsonAdapter =
        moshi.adapter(RecipeInstruction::class.java)


    @TypeConverter
    fun basicRoomRecipeToString(basicRoomRecipe: BasicRoomRecipe): String =
        basicRoomJsonAdapter.toJson(basicRoomRecipe)

    @TypeConverter
    fun stringToBasicRoomRecipe(data: String) =
        basicRoomJsonAdapter.fromJson(data)


    @TypeConverter
    fun selectedRecipeToString(selectedRecipe: SelectedRecipe): String =
        selectedRecipeJsonAdapter.toJson(selectedRecipe)

    @TypeConverter
    fun stringToSelectedRecipe(data: String) =
        selectedRecipeJsonAdapter.fromJson(data)


    @TypeConverter
    fun listOfRandomRecipeToString(randomRandomRecipes: List<RandomRecipe>): String =
        randomRandomRecipeJsonAdapter.toJson(randomRandomRecipes)

    @TypeConverter
    fun stringToListOfRandomRecipe(data: String) =
        randomRandomRecipeJsonAdapter.fromJson(data)

    @TypeConverter
    fun listOfRecipeInstructionToString(listOfRecipeInstruction: List<RecipeInstruction>): String =
        listOfRecipeInstructionJsonAdapter.toJson(listOfRecipeInstruction)

    @TypeConverter
    fun stringToListOfRecipeInstruction(data: String) =
        listOfRecipeInstructionJsonAdapter.fromJson(data)

    @TypeConverter
    fun stringToRecipeInstruction(data: String) =
        recipeInstructionJsonAdapter.fromJson(data)
}