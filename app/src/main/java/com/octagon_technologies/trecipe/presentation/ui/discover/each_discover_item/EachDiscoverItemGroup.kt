package com.octagon_technologies.trecipe.presentation.ui.discover.each_discover_item

import android.view.View
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.EachDiscoverItemLayoutBinding
import com.octagon_technologies.trecipe.repo.network.models.random_recipes.RandomRecipe
import com.octagon_technologies.trecipe.utils.ViewUtils
import com.xwray.groupie.viewbinding.BindableItem

class EachDiscoverItemGroup(val randomRecipe: RandomRecipe, private val onClick: (RandomRecipe) -> Unit): BindableItem<EachDiscoverItemLayoutBinding>() {
    override fun bind(binding: EachDiscoverItemLayoutBinding, position: Int) {
        binding.eachDiscoverName.text = randomRecipe.title
        binding.eachDiscoverSummary.text = ViewUtils.getHtmlFromString(randomRecipe.summary)

        ViewUtils.loadGlideImage(binding.eachDiscoverImage, randomRecipe.image)
        binding.root.setOnClickListener { onClick(randomRecipe) }
    }

    override fun getLayout() = R.layout.each_discover_item_layout

    override fun initializeViewBinding(view: View) =
        EachDiscoverItemLayoutBinding.bind(view)
}