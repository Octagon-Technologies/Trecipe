package com.octagontechnologies.trecipe.presentation.ui.search.recent_tab

import android.view.View
import com.octagontechnologies.trecipe.R
import com.octagontechnologies.trecipe.databinding.RecentAutocompleteTabBinding
import com.octagontechnologies.trecipe.domain.search.AutoComplete
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