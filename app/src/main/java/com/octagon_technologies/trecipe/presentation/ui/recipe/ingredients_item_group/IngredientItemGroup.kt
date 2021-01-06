package com.octagon_technologies.trecipe.presentation.ui.recipe.ingredients_item_group

import android.view.View
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.IngredientsItemLayoutBinding
import com.octagon_technologies.trecipe.repo.network.models.random_recipes.ExtendedIngredient
import com.xwray.groupie.viewbinding.BindableItem

class IngredientItemGroup(private val extendedIngredient: ExtendedIngredient): BindableItem<IngredientsItemLayoutBinding>() {
    override fun bind(binding: IngredientsItemLayoutBinding, position: Int) {
        binding.ingredientName.text = extendedIngredient.name
        binding.ingredientMeasure.text = "${extendedIngredient.amount} ${extendedIngredient.unit}"
    }

    override fun getLayout() = R.layout.ingredients_item_layout
    override fun initializeViewBinding(view: View) =
        IngredientsItemLayoutBinding.bind(view)
}