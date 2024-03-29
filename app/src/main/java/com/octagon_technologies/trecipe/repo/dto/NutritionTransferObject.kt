package com.octagon_technologies.trecipe.repo.dto

import com.octagon_technologies.trecipe.domain.recipe.nutrition.Flavonoid
import com.octagon_technologies.trecipe.domain.recipe.nutrition.Glycemic
import com.octagon_technologies.trecipe.domain.recipe.nutrition.Nutrient
import com.octagon_technologies.trecipe.domain.recipe.nutrition.NutritionDetails
import com.octagon_technologies.trecipe.repo.network.models.nutrition_details.NutritionDetailsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.MathContext

suspend fun NutritionDetailsResponse.toNutritionDetails(): NutritionDetails =
    withContext(Dispatchers.IO) {
        val nutrients =
            nutrients?.map {
                Nutrient(
                    name = it.name,
                    amount = it.amount.toBigDecimal().round(MathContext(2)).toDouble(),
                    unit = it.unit,
                    percentOfDailyNeeds = it.percentOfDailyNeeds
                )
            } ?: listOf()

        val flavonoids = remoteFlavonoids
            ?.filterNot {
                it.amount == 0.0
            }
            ?.map {
                Flavonoid(
                    name = it.name,
                    amount = it.amount.toBigDecimal().round(MathContext(2)).toDouble(),
                    unit = it.unit
                )
            } ?: listOf()
        val glycemic = Glycemic.fromProperties(properties)

        return@withContext NutritionDetails(
            nutrients, glycemic, flavonoids
        )
    }