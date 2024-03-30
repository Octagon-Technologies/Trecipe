package com.octagontechnologies.trecipe.repo.database.discover.try_out

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagontechnologies.trecipe.domain.discover.DiscoverRecipe
import com.octagontechnologies.trecipe.repo.database.BaseDao

@Dao
abstract class TryOutRecipeDao: BaseDao<TryOutRecipe> {

    @Query("SELECT * FROM tryOutRecipe")
    abstract suspend fun getTryOutRecipes(): TryOutRecipe?

}