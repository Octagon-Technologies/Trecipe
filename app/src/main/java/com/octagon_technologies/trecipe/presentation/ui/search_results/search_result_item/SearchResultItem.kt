package com.octagon_technologies.trecipe.presentation.ui.search_results.search_result_item

import android.view.View
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.SearchResultLayoutBinding
import com.octagon_technologies.trecipe.repo.network.models.complex_search.SearchResult
import com.octagon_technologies.trecipe.utils.ViewUtils
import com.xwray.groupie.viewbinding.BindableItem

class SearchResultItem(private val searchResult: SearchResult, private val onClickListener: (SearchResult) -> Unit): BindableItem<SearchResultLayoutBinding>() {
    override fun bind(binding: SearchResultLayoutBinding, position: Int) {
        binding.searchResultRecipeName.text = searchResult.title
        ViewUtils.loadGlideImage(binding.searchResultImage, searchResult.image)

        binding.root.setOnClickListener { onClickListener(searchResult) }
    }

    override fun getLayout() = R.layout.search_result_layout
    override fun initializeViewBinding(view: View) =
        SearchResultLayoutBinding.bind(view)
}