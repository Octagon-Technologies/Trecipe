package com.octagon_technologies.foodie.presentation.ui.discover.each_discover_item

import android.view.View
import com.octagon_technologies.foodie.R
import com.octagon_technologies.foodie.databinding.EachDiscoverItemLayoutBinding
import com.octagon_technologies.foodie.repo.network.models.recipes.Recipe
import com.octagon_technologies.foodie.utils.ViewUtils
import com.xwray.groupie.viewbinding.BindableItem

class EachDiscoverItemGroup(val recipe: Recipe, private val onClick: (Recipe) -> Unit): BindableItem<EachDiscoverItemLayoutBinding>() {
    override fun bind(binding: EachDiscoverItemLayoutBinding, position: Int) {
        binding.eachDiscoverName.text = recipe.title
        binding.eachDiscoverSummary.text = ViewUtils.getHtmlFromString(recipe.summary)

        ViewUtils.loadGlideImage(binding.eachDiscoverImage, recipe.image)
        binding.root.setOnClickListener { onClick(recipe) }
    }

    override fun getLayout() = R.layout.each_discover_item_layout

    override fun initializeViewBinding(view: View) =
        EachDiscoverItemLayoutBinding.bind(view)
}