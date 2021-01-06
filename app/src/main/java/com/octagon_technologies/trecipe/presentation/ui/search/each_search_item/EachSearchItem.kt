package com.octagon_technologies.trecipe.presentation.ui.search.each_search_item

import android.view.View
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.EachSearchItemLayoutBinding
import com.octagon_technologies.trecipe.repo.network.models.suggestions.RecipeSuggestion
import com.xwray.groupie.viewbinding.BindableItem

class EachSearchItem(
    private val recipeSuggestion: RecipeSuggestion,
    private val onClick: (RecipeSuggestion) -> Unit
) : BindableItem<EachSearchItemLayoutBinding>() {
    override fun bind(binding: EachSearchItemLayoutBinding, position: Int) {
        binding.eachSearchItemName.text = recipeSuggestion.title ?: "--"
        binding.root.setOnClickListener { onClick(recipeSuggestion) }
    }

    override fun getLayout() = R.layout.each_search_item_layout
    override fun initializeViewBinding(view: View) =
        EachSearchItemLayoutBinding.bind(view)
}