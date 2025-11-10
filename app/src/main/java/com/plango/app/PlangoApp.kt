package com.plango.app

import android.app.Application
import com.google.android.libraries.places.api.Places

class PlangoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyBpfnjMYTTZbdd2cny-CP9kyrXASGaUgz0")
        }
    }
}
