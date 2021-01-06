package com.octagon_technologies.trecipe.presentation.ui.search_results.filter_tab_item

import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.databinding.FilterTabLayoutBinding
import com.octagon_technologies.trecipe.utils.ResUtils
import com.octagon_technologies.trecipe.utils.ViewUtils
import com.xwray.groupie.viewbinding.BindableItem

class FilterTabItem(
    private val onCloseBtnClicked: () -> Unit,
) : BindableItem<FilterTabLayoutBinding>() {
    override fun bind(binding: FilterTabLayoutBinding, position: Int) {
        val context = binding.root.context
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_cancel)

        binding.root.updatePadding(right = ResUtils.getPixelsFromSdp(context, R.dimen._10sdp))
        binding.root.compoundDrawablePadding = ResUtils.getPixelsFromSdp(context, R.dimen._10sdp)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            binding.root.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
        } else {
            drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            binding.root.setCompoundDrawables(null, null, drawable, null)
        }

        binding.root.setOnClickListener { onCloseBtnClicked() }
    }

    override fun getLayout() = R.layout.filter_tab_layout
    override fun initializeViewBinding(view: View) =
        FilterTabLayoutBinding.bind(view)
}