package com.x930073498.sample

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.x930073498.features.core.application.ApplicationFeatureInstaller
import com.x930073498.features.installApplicationFeature
import com.x930073498.features.installFeature

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        installFeature(TestFeatureInstaller())
        installApplicationFeature(ApplicationFeature, ApplicationInstaller)
        println("enter this line App")
    }
}