package com.octagontechnologies.trecipe.domain.recipe.nutrition

import com.octagontechnologies.trecipe.repo.network.models.nutrition_details.Property
import java.math.MathContext

data class Glycemic(
    val index: Double?,
    val load: Double?
) {

    companion object {
        const val INDEX = "Glycemic Index"
        const val LOAD = "Glycemic Load"

        fun fromProperties(properties: List<Property>?): Glycemic {
            val index = properties?.find { it.name == INDEX }?.amount?.toBigDecimal(MathContext(2))?.toDouble()
            val load = properties?.find { it.name == LOAD }?.amount?.toBigDecimal(MathContext(2))?.toDouble()

            return Glycemic(index, load)
        }
    }

}
