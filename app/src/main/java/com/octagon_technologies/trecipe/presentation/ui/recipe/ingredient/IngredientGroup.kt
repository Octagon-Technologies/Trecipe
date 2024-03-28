package com.octagon_technologies.trecipe.presentation.ui.recipe.ingredient

import android.view.View
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.MiniIngredientBinding
import com.octagon_technologies.trecipe.domain.recipe.Ingredient
import com.octagon_technologies.trecipe.utils.loadImage
import com.xwray.groupie.viewbinding.BindableItem
import timber.log.Timber

class IngredientGroup(
    private val ingredient: Ingredient,
    private val isUs: Boolean,
    private val exploreIngredient: (Int) -> Unit
) : BindableItem<MiniIngredientBinding>() {

    override fun bind(binding: MiniIngredientBinding, position: Int) {
        Timber.d("ingredient.ingredientImage is ${ingredient.ingredientImage}")
        binding.ingredientImage.loadImage(ingredient.ingredientImage, R.drawable.loading_food)
        binding.ingredientName.text = ingredient.name
        binding.ingredientAmount.text = ingredient.toFormattedAmount(isUs)
        binding.exploreIngredient.setOnClickListener { exploreIngredient(ingredient.id) }
    }

    override fun getLayout(): Int = R.layout.mini_ingredient
    override fun initializeViewBinding(view: View): MiniIngredientBinding =
        MiniIngredientBinding.bind(view)
}