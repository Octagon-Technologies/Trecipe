package com.octagon_technologies.trecipe.presentation.ui.search.autocomplete_group

import android.view.View
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.AutocompleteCardBinding
import com.octagon_technologies.trecipe.domain.search.AutoComplete
import com.xwray.groupie.viewbinding.BindableItem

class AutoCompleteGroup(
    private val autoComplete: AutoComplete,
    private val onClick: () -> Unit
) : BindableItem<AutocompleteCardBinding>() {
    override fun bind(binding: AutocompleteCardBinding, position: Int) {
        binding.eachSearchItemName.text = autoComplete.name
        binding.root.setOnClickListener { onClick() }
    }

    override fun getLayout() = R.layout.autocomplete_card
    override fun initializeViewBinding(view: View) =
        AutocompleteCardBinding.bind(view)
}