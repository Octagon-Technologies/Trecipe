package com.octagon_technologies.foodie.presentation.ui.discover.ad_discover_item

import android.view.View
import com.octagon_technologies.foodie.R
import com.octagon_technologies.foodie.ads.AdHelper
import com.octagon_technologies.foodie.databinding.AdDiscoverPageBinding
import com.xwray.groupie.viewbinding.BindableItem

class AdDiscoverItem(private val adHelper: AdHelper, private val onFailure: (Int) -> Unit): BindableItem<AdDiscoverPageBinding>() {
    var currentPosition = -1

    override fun bind(binding: AdDiscoverPageBinding, position: Int) {
        currentPosition = position

        // Loads ad and calls onFailure() if it fails
        adHelper.loadAd(binding) { isSuccess ->
            if (!isSuccess) {
                onFailure(currentPosition)
            }
        }
    }

    override fun getLayout() = R.layout.ad_discover_page
    override fun initializeViewBinding(view: View) =
        AdDiscoverPageBinding.bind(view)
}