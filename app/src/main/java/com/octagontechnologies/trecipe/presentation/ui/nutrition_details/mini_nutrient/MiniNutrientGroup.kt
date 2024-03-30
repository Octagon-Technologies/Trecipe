package com.octagontechnologies.trecipe.presentation.ui.nutrition_details.mini_nutrient

import android.view.View
import com.octagontechnologies.trecipe.R
import com.octagontechnologies.trecipe.databinding.MiniNutritionLayoutBinding
import com.octagontechnologies.trecipe.domain.recipe.nutrition.Nutrient
import com.xwray.groupie.viewbinding.BindableItem

/**
 * Item ViewHolder for each nutrient and flavonoid
 *
 * The trick we are pulling to accommodate both nutrient and flavonoid(doesn't have daily
 * percentage needs) is making the percent nullable
 *
 * IF null, display a "-- %" on the textview, otherwise show the actual figure
 */
class MiniNutrientGroup(private val nutrient: Nutrient): BindableItem<MiniNutritionLayoutBinding>() {

    override fun bind(binding: MiniNutritionLayoutBinding, position: Int) {
        binding.nutrientAmount.text = nutrient.amountWithUnit
        binding.nutrientName.text = nutrient.name
        binding.nutrientDailyPercent.text = nutrient.formattedPercentOfDailyNeeds ?: "-- %"
    }

    override fun getLayout(): Int = R.layout.mini_nutrition_layout
    override fun initializeViewBinding(view: View) =
        MiniNutritionLayoutBinding.bind(view)
}