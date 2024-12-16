package com.datasite.luditotest.app

import android.app.Application
import com.datasite.luditotest.cache.AppCache
import com.yandex.mapkit.MapKitFactory

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("522fb9ba-acc3-4c2a-ad64-371448cace44")
        MapKitFactory.initialize(this)

        instance = this
        AppCache.init(this)

    }
        companion object {
            var instance: Application? = null
                private set
        }
}