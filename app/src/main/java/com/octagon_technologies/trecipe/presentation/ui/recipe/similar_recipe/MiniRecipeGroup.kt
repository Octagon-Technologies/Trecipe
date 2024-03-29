package com.octagon_technologies.trecipe.presentation.ui.recipe.similar_recipe

import android.view.View
import android.widget.ImageView
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.MiniRecipeLayoutBinding
import com.octagon_technologies.trecipe.domain.similar_recipe.SimilarRecipe
import com.octagon_technologies.trecipe.domain.similar_recipe.toRecipeRating
import com.octagon_technologies.trecipe.domain.similar_recipe.toRecipeTime
import com.octagon_technologies.trecipe.utils.loadImage
import com.xwray.groupie.viewbinding.BindableItem
import timber.log.Timber

class MiniRecipeGroup(
    private val similarRecipe: SimilarRecipe,
    private val openRecipe: () -> Unit,
    private var isSaved: Boolean,
    private val saveOrUnsaveRecipe: () -> Unit
) : BindableItem<MiniRecipeLayoutBinding>() {
    override fun bind(binding: MiniRecipeLayoutBinding, position: Int) {
        // In the Similar recipes for RecipeFragment, we have no image
        binding.miniRecipeImage.loadImage(similarRecipe.imagePath, R.drawable.loading_food)
        binding.miniRecipeName.text = similarRecipe.recipeName
        binding.miniRecipeTime.text = similarRecipe.toRecipeTime()
        binding.miniRecipeRating.rating = similarRecipe.toRecipeRating()

        binding.root.setOnClickListener { openRecipe() }

        binding.saveBtn.updateSaveImage()
        binding.saveBtn.setOnClickListener {
            isSaved = !isSaved

            binding.saveBtn.updateSaveImage()
            saveOrUnsaveRecipe()
        }
    }

    private fun ImageView.updateSaveImage() =
        setImageResource(
            if (isSaved) R.drawable.save_filled else R.drawable.save_border
        )

    override fun getLayout() = R.layout.mini_recipe_layout
    override fun initializeViewBinding(view: View): MiniRecipeLayoutBinding =
        MiniRecipeLayoutBinding.bind(view)
}