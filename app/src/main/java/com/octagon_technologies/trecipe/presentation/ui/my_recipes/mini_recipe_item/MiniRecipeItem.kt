package com.octagon_technologies.trecipe.presentation.ui.my_recipes.mini_recipe_item

import android.view.View
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.MiniRecipeLayoutBinding
import com.octagon_technologies.trecipe.models.BasicRoomRecipe
import com.octagon_technologies.trecipe.repo.network.models.selected_recipe.SelectedRecipe
import com.octagon_technologies.trecipe.utils.ViewUtils
import com.xwray.groupie.viewbinding.BindableItem

class MiniRecipeItem(
    val any: Any,
    private val onClickLambda: (Any) -> Unit,
    private val removeRecipeLambda: (MiniRecipeItem) -> Unit,
) : BindableItem<MiniRecipeLayoutBinding>() {
    var currentPosition = -1

    override fun bind(binding: MiniRecipeLayoutBinding, position: Int) {
        currentPosition = position

        // Any is either BasicRoomRecipe or SelectedRecipe
        val basicRoomRecipe =
            if (any is BasicRoomRecipe) any
            else (any as SelectedRecipe).toBasicRoomRecipe()

        binding.miniRecipeName.text = basicRoomRecipe.name
        ViewUtils.loadGlideImage(binding.miniRecipeImage, basicRoomRecipe.imageUrl)

        binding.root.setOnClickListener { onClickLambda(any) }
        binding.removeBtn.setOnClickListener { removeRecipeLambda(this) }
    }


    override fun bind(
        viewBinding: MiniRecipeLayoutBinding,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        payloads.firstOrNull()?.let {
            currentPosition = it as Int
        } ?: run {
            super.bind(viewBinding, position, payloads)
        }
    }

    override fun getLayout() = R.layout.mini_recipe_layout
    override fun initializeViewBinding(view: View) =
        MiniRecipeLayoutBinding.bind(view)
}