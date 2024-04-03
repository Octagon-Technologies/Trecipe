package com.octagontechnologies.trecipe.presentation.ui.search_results.search_result

import android.view.View
import com.google.android.material.imageview.ShapeableImageView
import com.octagontechnologies.trecipe.R
import com.octagontechnologies.trecipe.databinding.MiniRecipeLayoutBinding
import com.octagontechnologies.trecipe.domain.search.SimpleRecipe
import com.octagontechnologies.trecipe.utils.loadImage
import com.xwray.groupie.viewbinding.BindableItem

class SearchResultGroup(
    private val searchResult: SimpleRecipe,
    private val openRecipe: () -> Unit,
    private var isSaved: Boolean,
    private val saveOrUnsaveRecipe: () -> Unit
) : BindableItem<MiniRecipeLayoutBinding>() {
    override fun bind(binding: MiniRecipeLayoutBinding, position: Int) {
        binding.miniRecipeName.text = searchResult.recipeName
        binding.miniRecipeImage.loadImage(searchResult.recipeImage, R.drawable.loading_food)

        binding.miniRecipeRating.visibility = View.GONE
        binding.miniRecipeTime.visibility = View.GONE

        binding.saveBtn.updateSaveBtn()

        binding.saveBtn.setOnClickListener {
            isSaved = !isSaved
            binding.saveBtn.updateSaveBtn()
            saveOrUnsaveRecipe()
        }
        binding.root.setOnClickListener { openRecipe() }
    }

    private fun ShapeableImageView.updateSaveBtn() {
        setImageResource(
            if(isSaved) R.drawable.save_filled else R.drawable.save_border
        )
    }

    override fun getLayout() = R.layout.mini_recipe_layout
    override fun initializeViewBinding(view: View) =
        MiniRecipeLayoutBinding.bind(view)
}