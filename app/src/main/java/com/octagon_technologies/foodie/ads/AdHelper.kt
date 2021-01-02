package com.octagon_technologies.foodie.ads

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.octagon_technologies.foodie.R
import com.octagon_technologies.foodie.databinding.AdDiscoverPageBinding
import com.octagon_technologies.foodie.utils.Constants
import timber.log.Timber
import java.util.*

class AdHelper(private val activity: AppCompatActivity) {
    private val builder = AdLoader.Builder(activity.applicationContext, Constants.TEST_AD_ID)
    private var currentNativeAd: UnifiedNativeAd? = null

    fun loadAd(adDiscoverPageBinding: AdDiscoverPageBinding, onComplete: (Boolean) -> Unit) {
        try {
            getAdLoader(adDiscoverPageBinding, onComplete)?.loadAd(getAdRequest())
        } catch (e: Throwable) {
            Timber.e(e, "Evil exception hiding in ads")
        }
    }

    private fun getAdLoader(
        adDiscoverPageBinding: AdDiscoverPageBinding,
        onComplete: (Boolean) -> Unit
    ): AdLoader? {
        return builder
            .forUnifiedNativeAd { unifiedNativeAd ->
                // If this callback occurs after the activity is destroyed, you must call
                // destroy and return or you may get a memory leak.

                var activityDestroyed = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    activityDestroyed = activity.isDestroyed
                }
                if (activityDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                    unifiedNativeAd.destroy()
                    return@forUnifiedNativeAd
                }
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                currentNativeAd?.destroy()
                currentNativeAd = unifiedNativeAd
                populateUnifiedNativeAdView(unifiedNativeAd, adDiscoverPageBinding, onComplete)
//                displayUnifiedNativeAd(nativeAdsLayoutBinding, currentNativeAd)
            }
            .withAdListener(object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    onComplete(true)
                }

                override fun onAdFailedToLoad(p0: LoadAdError?) {
                    super.onAdFailedToLoad(p0)
                    onComplete(false)
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_BOTTOM_RIGHT)
                    .setRequestCustomMuteThisAd(true)
                    .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_SQUARE)
                    .build()
            )
            .build()
    }

    private fun getAdRequest(): AdRequest =
        AdRequest.Builder().build()

//    private fun displayUnifiedNativeAd(nativeAdsLayoutBinding: NativeAdsLayoutBinding, ad: UnifiedNativeAd?) {
//        if (ad == null) return
//
//        val root = (nativeAdsLayoutBinding.root as UnifiedNativeAdView).also{
//            it.mediaView = nativeAdsLayoutBinding.adMediaView
//        }
//
//        root.mediaView.setMediaContent(ad.mediaContent)
//        Timber.d("ad.price is ${ad.price}")
//        Timber.d("ad.mediaContent.isNull() is ${ad.mediaContent.isNull()}")
//
//        // Call the UnifiedNativeAdView's setNativeAd method to register the
//        // NativeAdObject.
//        root.setNativeAd(ad)
//    }

    /**
     * Populates a [UnifiedNativeAdView] object with data from a given
     * [UnifiedNativeAd].
     *
     * @param nativeAd the object containing the ad's assets
     * @param adDiscoverPageBinding the binding to be populated
     */
    private fun populateUnifiedNativeAdView(
        nativeAd: UnifiedNativeAd,
        adDiscoverPageBinding: AdDiscoverPageBinding,
        onComplete: (Boolean) -> Unit
    ) {
        val adView = adDiscoverPageBinding.adView

        // Set the media view.
        adView.mediaView = adView.findViewById(R.id.ad_media)

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
//        adView.bodyView = adView.findViewById(R.id.ad_body)
//        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
//        adView.iconView = adView.findViewById(R.id.ad_app_icon)
//        adView.priceView = adView.findViewById(R.id.ad_price)
//        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        adView.mediaView.setMediaContent(nativeAd.mediaContent)

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
//        if (nativeAd.body == null) {
//            adView.bodyView.visibility = View.INVISIBLE
//        } else {
//            adView.bodyView.visibility = View.VISIBLE
//            (adView.bodyView as TextView).text = nativeAd.body
//        }

//        if (nativeAd.callToAction == null) {
//            adView.callToActionView.visibility = View.INVISIBLE
//        } else {
//            adView.callToActionView.visibility = View.VISIBLE
//            (adView.callToActionView as Button).text = nativeAd.callToAction
//        }
//
//        if (nativeAd.icon == null) {
//            adView.iconView.visibility = View.GONE
//        } else {
//            (adView.iconView as ImageView).setImageDrawable(
//                nativeAd.icon.drawable
//            )
//            adView.iconView.visibility = View.VISIBLE
//        }
//
//        if (nativeAd.price == null) {
//            adView.priceView.visibility = View.INVISIBLE
//        } else {
//            adView.priceView.visibility = View.VISIBLE
//            (adView.priceView as TextView).text = nativeAd.price
//        }

        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }

//        if (nativeAd.starRating == null) {
//            adView.starRatingView.visibility = View.INVISIBLE
//        } else {
//            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
//            adView.starRatingView.visibility = View.VISIBLE
//        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        val vc = nativeAd.videoController

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            Timber.d("Video aspectRatio is ${vc.aspectRatio}")

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                override fun onVideoEnd() {
                    loadAd(adDiscoverPageBinding, onComplete)
                    super.onVideoEnd()
                }
            }
        }
    }
}