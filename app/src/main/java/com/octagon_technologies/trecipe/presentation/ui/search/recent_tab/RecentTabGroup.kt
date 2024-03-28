package com.octagon_technologies.trecipe.presentation.ui.search.recent_tab

import android.view.View
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.RecentAutocompleteTabBinding
import com.octagon_technologies.trecipe.domain.search.AutoComplete
import com.xwray.groupie.viewbinding.BindableItem

class RecentTabGroup(private val recentAutoComplete: AutoComplete, private val onRecentTabSelected: () -> Unit): BindableItem<RecentAutocompleteTabBinding>() {
    override fun bind(binding: RecentAutocompleteTabBinding, position: Int) {
        binding.searchName.text = recentAutoComplete.name
        binding.root.setOnClickListener { onRecentTabSelected() }
    }

    override fun getLayout(): Int = R.layout.recent_autocomplete_tab
    override fun initializeViewBinding(view: View) =
        RecentAutocompleteTabBinding.bind(view)
}