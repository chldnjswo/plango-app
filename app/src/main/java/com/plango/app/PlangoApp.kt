package com.plango.app

import android.app.Application
import android.util.Log
import com.google.android.libraries.places.api.Places

class PlangoApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val apiKey = BuildConfig.MAPS_API_KEY



        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
    }
}
