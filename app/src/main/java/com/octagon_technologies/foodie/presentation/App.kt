package com.octagon_technologies.foodie.presentation

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.octagon_technologies.foodie.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        
        MobileAds.initialize(applicationContext)
    }
}