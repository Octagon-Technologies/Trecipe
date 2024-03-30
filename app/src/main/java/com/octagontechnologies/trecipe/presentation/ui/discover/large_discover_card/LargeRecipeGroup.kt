package com.octagontechnologies.trecipe.presentation.ui.discover.large_discover_card

import android.media.Image
import android.view.View
import android.widget.ImageView
import androidx.core.text.HtmlCompat
import com.octagontechnologies.trecipe.R
import com.octagontechnologies.trecipe.databinding.LargeRecipeCardBinding
import com.octagontechnologies.trecipe.domain.discover.DiscoverRecipe
import com.octagontechnologies.trecipe.domain.discover.toRecipeRating
import com.octagontechnologies.trecipe.domain.discover.toRecipeTime
import com.octagontechnologies.trecipe.utils.loadImage
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
        binding.saveBtn.updateSaveImage()

        binding.saveBtn.setOnClickListener {
            isSaved = !isSaved
            binding.saveBtn.updateSaveImage()
            saveOrUnsaveRecipe()
        }
    }

    private fun ImageView.updateSaveImage() {
        setImageResource(
            if (isSaved) R.drawable.save_filled else R.drawable.save_border
        )
    }


    override fun getLayout() = R.layout.large_recipe_card
    override fun initializeViewBinding(view: View) =
        LargeRecipeCardBinding.bind(view)
}