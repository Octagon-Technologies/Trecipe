package com.octagontechnologies.trecipe.presentation.ui.search_results.search_result

import android.view.View
import com.octagontechnologies.trecipe.R
import com.octagontechnologies.trecipe.databinding.MiniRecipeLayoutBinding
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe
import com.octagontechnologies.trecipe.utils.loadImage
import com.xwray.groupie.viewbinding.BindableItem

class SearchResultGroup(
    private val searchResult: SimpleRecipe,
    private val openRecipe: () -> Unit
) : BindableItem<MiniRecipeLayoutBinding>() {
    override fun bind(binding: MiniRecipeLayoutBinding, position: Int) {
        binding.miniRecipeName.text = searchResult.recipeName
        binding.miniRecipeImage.loadImage(searchResult.recipeImage, R.drawable.loading_food)

        binding.miniRecipeRating.visibility = View.GONE
        binding.miniRecipeTime.visibility = View.GONE

        binding.root.setOnClickListener { openRecipe() }
    }

    override fun getLayout() = R.layout.mini_recipe_layout
    override fun initializeViewBinding(view: View) =
        MiniRecipeLayoutBinding.bind(view)
}