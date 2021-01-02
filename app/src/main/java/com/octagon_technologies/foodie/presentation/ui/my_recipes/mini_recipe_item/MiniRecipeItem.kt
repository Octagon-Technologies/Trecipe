package com.octagon_technologies.foodie.presentation.ui.my_recipes.mini_recipe_item

import android.view.View
import com.octagon_technologies.foodie.R
import com.octagon_technologies.foodie.databinding.MiniRecipeLayoutBinding
import com.octagon_technologies.foodie.models.BasicRoomRecipe
import com.octagon_technologies.foodie.utils.ViewUtils
import com.xwray.groupie.viewbinding.BindableItem

class MiniRecipeItem(val basicRoomRecipe: BasicRoomRecipe): BindableItem<MiniRecipeLayoutBinding>() {
    override fun bind(binding: MiniRecipeLayoutBinding, position: Int) {
        binding.miniRecipeName.text = basicRoomRecipe.name
        ViewUtils.loadGlideImage(binding.miniRecipeImage, basicRoomRecipe.imageUrl)
    }

    override fun getLayout() = R.layout.mini_recipe_layout
    override fun initializeViewBinding(view: View) =
        MiniRecipeLayoutBinding.bind(view)
}