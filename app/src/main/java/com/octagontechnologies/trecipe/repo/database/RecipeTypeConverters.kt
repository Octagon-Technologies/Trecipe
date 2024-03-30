package com.octagontechnologies.trecipe.repo.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.octagontechnologies.trecipe.domain.discover.DiscoverRecipe
import com.octagontechnologies.trecipe.domain.recipe.RecipeDetails
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe
import com.octagontechnologies.trecipe.repo.network.models.instructions.RecipeInstruction
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@TypeConverters
class RecipeTypeConverters {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val recipeDetailsJsonAdapter: JsonAdapter<RecipeDetails> =
        moshi.adapter(RecipeDetails::class.java)

    private val simpleRecipeJsonAdapter: JsonAdapter<SimpleRecipe> =
        moshi.adapter(SimpleRecipe::class.java)


    private val discoverRecipeJsonAdapter: JsonAdapter<List<DiscoverRecipe>> =
        moshi.adapter(Types.newParameterizedType(List::class.java, DiscoverRecipe::class.java))

//    private val listOfRecipeInstructionJsonAdapter: JsonAdapter<List<RecipeInstruction>> =
//        moshi.adapter(Types.newParameterizedType(List::class.java, RecipeInstruction::class.java))
//
//    private val recipeInstructionJsonAdapter =
//        moshi.adapter(RecipeInstruction::class.java)


    @TypeConverter
    fun recipeDetailsToString(recipeDetails: RecipeDetails): String =
        recipeDetailsJsonAdapter.toJson(recipeDetails)

    @TypeConverter
    fun stringToRecipeDetails(data: String) =
        recipeDetailsJsonAdapter.fromJson(data)

    @TypeConverter
    fun simpleRecipeToString(simpleRecipe: SimpleRecipe): String =
        simpleRecipeJsonAdapter.toJson(simpleRecipe)

    @TypeConverter
    fun stringToSimpleRecipe(data: String) =
        simpleRecipeJsonAdapter.fromJson(data)


    @TypeConverter
    fun listOfDiscoverRecipeToString(recipeList: List<DiscoverRecipe>): String =
        discoverRecipeJsonAdapter.toJson(recipeList)

    @TypeConverter
    fun stringToListOfDiscoverRecipe(data: String) =
        discoverRecipeJsonAdapter.fromJson(data)

//    @TypeConverter
//    fun listOfRecipeInstructionToString(listOfRecipeInstruction: List<RecipeInstruction>): String =
//        listOfRecipeInstructionJsonAdapter.toJson(listOfRecipeInstruction)
//
//    @TypeConverter
//    fun stringToListOfRecipeInstruction(data: String) =
//        listOfRecipeInstructionJsonAdapter.fromJson(data)
//
//    @TypeConverter
//    fun stringToRecipeInstruction(data: String) =
//        recipeInstructionJsonAdapter.fromJson(data)
}