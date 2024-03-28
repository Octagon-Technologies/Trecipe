package com.octagon_technologies.trecipe.repo.database.discover.try_out

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagon_technologies.trecipe.domain.discover.DiscoverRecipe
import com.octagon_technologies.trecipe.repo.database.BaseDao

@Dao
abstract class TryOutRecipeDao: BaseDao<TryOutRecipe> {

    @Query("SELECT * FROM tryOutRecipe")
    abstract suspend fun getTryOutRecipes(): TryOutRecipe?

}