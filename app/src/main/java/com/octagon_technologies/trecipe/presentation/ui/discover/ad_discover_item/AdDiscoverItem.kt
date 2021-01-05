package com.octagon_technologies.trecipe.presentation.ui.discover.ad_discover_item

//class AdDiscoverItem(private val adHelper: AdHelper, private val onFailure: (Int) -> Unit): BindableItem<AdDiscoverPageBinding>() {
//    var currentPosition = -1
//
//    override fun bind(binding: AdDiscoverPageBinding, position: Int) {
//        currentPosition = position
//
//        // Loads ad and calls onFailure() if it fails
//        adHelper.loadAd(binding) { isSuccess ->
//            if (!isSuccess) {
//                onFailure(currentPosition)
//            }
//        }
//    }
//
//    override fun getLayout() = R.layout.ad_discover_page
//    override fun initializeViewBinding(view: View) =
//        AdDiscoverPageBinding.bind(view)
//}