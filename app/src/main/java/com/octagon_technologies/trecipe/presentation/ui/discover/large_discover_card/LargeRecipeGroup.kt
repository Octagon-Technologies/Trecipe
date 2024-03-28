package com.octagon_technologies.trecipe.presentation.ui.discover.large_discover_card

import android.view.View
import androidx.core.text.HtmlCompat
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.LargeRecipeCardBinding
import com.octagon_technologies.trecipe.domain.discover.DiscoverRecipe
import com.octagon_technologies.trecipe.domain.discover.toRecipeRating
import com.octagon_technologies.trecipe.domain.discover.toRecipeTime
import com.octagon_technologies.trecipe.utils.loadImage
import com.xwray.groupie.viewbinding.BindableItem

class LargeRecipeGroup(
    private val discoverRecipe: DiscoverRecipe,
    private val openRecipe: () -> Unit,
    private var isSaved: Boolean,
    private val saveOrUnsaveRecipe: () -> Unit
) : BindableItem<LargeRecipeCardBinding>() {
    override fun bind(binding: LargeRecipeCardBinding, position: Int) {
        binding.largeRecipeName.text = discoverRecipe.recipeName
        binding.largeRecipeSummary.text = HtmlCompat.fromHtml(
            discoverRecipe.recipeSummary,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.largeRecipeImage.loadImage(discoverRecipe.recipeImage, R.drawable.loading_food)

        binding.largeRecipeRating.rating = discoverRecipe.toRecipeRating()
        binding.largeRecipeTime.text = discoverRecipe.toRecipeTime()

        binding.root.setOnClickListener { openRecipe() }

        binding.saveBtn.setImageResource(getSaveImage())
        binding.saveBtn.setOnClickListener {
            isSaved = !isSaved
            getSaveImage()
            saveOrUnsaveRecipe()
        }
    }

    private fun getSaveImage() = if (isSaved) R.drawable.save_filled else R.drawable.save_border


    override fun getLayout() = R.layout.large_recipe_card
    override fun initializeViewBinding(view: View) =
        LargeRecipeCardBinding.bind(view)
}