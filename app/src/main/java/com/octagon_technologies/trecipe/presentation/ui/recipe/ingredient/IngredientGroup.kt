package com.octagon_technologies.trecipe.presentation.ui.recipe.ingredient

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.MiniIngredientBinding
import com.octagon_technologies.trecipe.domain.recipe.Ingredient
import com.xwray.groupie.viewbinding.BindableItem
import timber.log.Timber

class IngredientGroup(
    private val ingredient: Ingredient,
    private val isUs: Boolean,
    private val exploreIngredient: (Int) -> Unit
) : BindableItem<MiniIngredientBinding>() {

    override fun bind(binding: MiniIngredientBinding, position: Int) {
        Timber.d("ingredient.ingredientImage is ${ingredient.ingredientImage}")
        binding.ingredientImage.loadIngredientImage(ingredient.ingredientImage)
        binding.ingredientName.text = ingredient.name
        binding.ingredientAmount.text = ingredient.toFormattedAmount(isUs)
        binding.exploreIngredient.setOnClickListener { exploreIngredient(ingredient.id) }
    }

    private fun ImageView.loadIngredientImage(icon: String?) {
        Glide.with(this)
            .load(icon)
            .placeholder(R.drawable.loading_food)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(this)
    }

    override fun getLayout(): Int = R.layout.mini_ingredient
    override fun initializeViewBinding(view: View): MiniIngredientBinding =
        MiniIngredientBinding.bind(view)
}