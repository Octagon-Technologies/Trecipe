package com.octagon_technologies.trecipe.repo.database.discover.daily

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.octagon_technologies.trecipe.domain.discover.DiscoverRecipe
import com.octagon_technologies.trecipe.repo.database.BaseDao

@Dao
abstract class DailyRecipeDao: BaseDao<DailyLocalRecipe> {

    @Query("SELECT * FROM dailyLocalRecipe")
    abstract fun getDailyRecipes(): LiveData<DailyLocalRecipe?>

    @Query("SELECT * FROM dailyLocalRecipe")
    abstract suspend fun getDailyRecipesOneShot(): DailyLocalRecipe?

}