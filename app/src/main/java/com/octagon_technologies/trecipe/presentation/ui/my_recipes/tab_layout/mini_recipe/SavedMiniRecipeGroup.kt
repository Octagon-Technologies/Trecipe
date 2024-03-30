package com.octagon_technologies.trecipe.presentation.ui.my_recipes.tab_layout.mini_recipe

import android.view.View
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.MiniRecipeLayoutBinding
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe
import com.octagon_technologies.trecipe.utils.loadImage
import com.xwray.groupie.viewbinding.BindableItem

class SavedMiniRecipeGroup(
    private val simpleRecipe: SimpleRecipe,
    private val openRecipe: () -> Unit,
    private val removeFromCategory: () -> Unit
): BindableItem<MiniRecipeLayoutBinding>() {

    /**
     * This is the group we'll use across Recent, Saved and Liked tabs.
     * Therefore, we change the Saved button to a Minus button which the user presses to remove
     * to remove the recipe from the respective category
     *
     * KINDA messed up but sorryyyyyy, future maintainer :)
     */
    override fun bind(binding: MiniRecipeLayoutBinding, position: Int) {
        binding.miniRecipeName.text = simpleRecipe.recipeName
        binding.miniRecipeImage.loadImage(simpleRecipe.recipeImage, R.drawable.loading_food)
        binding.saveBtn.setImageResource(R.drawable.minus)
        binding.saveBtn.setOnClickListener { removeFromCategory() }

        binding.miniRecipeRating.visibility = View.GONE
        binding.miniRecipeTime.visibility = View.GONE

        binding.saveBtn.setOnClickListener { removeFromCategory() }

        binding.root.setOnClickListener { openRecipe() }
    }

    override fun getLayout(): Int = R.layout.mini_recipe_layout
    override fun initializeViewBinding(view: View): MiniRecipeLayoutBinding =
        MiniRecipeLayoutBinding.bind(view)
}