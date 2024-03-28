package com.octagon_technologies.trecipe.presentation.ui.my_recipes.tab_layout.mini_recipe

import android.view.View
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.MiniRecipeLayoutBinding
import com.octagon_technologies.trecipe.domain.search.SimpleRecipe
import com.octagon_technologies.trecipe.utils.loadImage
import com.xwray.groupie.viewbinding.BindableItem

class SavedMiniRecipeGroup(private val simpleRecipe: SimpleRecipe, private var isSaved: Boolean, private val saveOrUnSaveRecipe: () -> Unit): BindableItem<MiniRecipeLayoutBinding>() {

    override fun bind(binding: MiniRecipeLayoutBinding, position: Int) {
        binding.miniRecipeName.text = simpleRecipe.recipeName
        binding.miniRecipeImage.loadImage(simpleRecipe.recipeImage, R.drawable.loading_food)
        binding.saveBtn.setImageResource(getSaveImage())
        binding.saveBtn.setOnClickListener { saveOrUnSaveRecipe() }

        binding.miniRecipeRating.visibility = View.GONE
        binding.miniRecipeTime.visibility = View.GONE

        binding.saveBtn.setOnClickListener {
            isSaved = !isSaved
            getSaveImage()
            saveOrUnSaveRecipe()
        }
    }

    private fun getSaveImage() = if (isSaved) R.drawable.save_filled else R.drawable.save_border

    override fun getLayout(): Int = R.layout.mini_recipe_layout
    override fun initializeViewBinding(view: View): MiniRecipeLayoutBinding =
        MiniRecipeLayoutBinding.bind(view)
}