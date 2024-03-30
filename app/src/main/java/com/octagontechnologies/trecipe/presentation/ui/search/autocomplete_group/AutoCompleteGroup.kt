package com.octagontechnologies.trecipe.presentation.ui.search.autocomplete_group

import android.view.View
import com.octagontechnologies.trecipe.R
import com.octagontechnologies.trecipe.databinding.AutocompleteCardBinding
import com.octagontechnologies.trecipe.domain.search.AutoComplete
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