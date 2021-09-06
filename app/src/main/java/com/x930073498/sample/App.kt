package com.x930073498.sample

import android.app.Application
import com.x930073498.features.core.application.ApplicationFeatureLifecycleObserver
import com.x930073498.features.installApplicationFeature

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        installApplicationFeature(TestFeature) {
            this.target.featureLifecycle.addObserver(object :ApplicationFeatureLifecycleObserver{
                override fun onApplicationPaused() {
                    feature.print()
                }
            })
        }
    }

}