package com.octagon_technologies.foodie.repo.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.octagon_technologies.foodie.models.BasicRoomRecipe
import com.octagon_technologies.foodie.repo.network.models.recipes.Recipe
import com.octagon_technologies.foodie.repo.network.models.selected_recipe.SelectedRecipe
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@TypeConverters
class RecipeTypeConverters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val basicRoomJsonAdapter: JsonAdapter<BasicRoomRecipe> =
        moshi.adapter(BasicRoomRecipe::class.java)

    private val selectedRecipeJsonAdapter: JsonAdapter<SelectedRecipe> =
        moshi.adapter(SelectedRecipe::class.java)

    private val randomRecipeJsonAdapter: JsonAdapter<List<Recipe>> =
        moshi.adapter(Types.newParameterizedType(List::class.java, Recipe::class.java))


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
    fun listOfRandomRecipeToString(randomRecipes: List<Recipe>): String =
        randomRecipeJsonAdapter.toJson(randomRecipes)
    @TypeConverter
    fun stringToListOfRandomRecipe(data: String) =
        randomRecipeJsonAdapter.fromJson(data)
}