package com.x930073498.sample

import com.x930073498.features.core.Feature
import com.x930073498.features.core.application.ApplicationFeatureInstaller
import com.x930073498.features.core.application.ApplicationFeatureLifecycle
import com.x930073498.features.core.application.ApplicationFeatureLifecycleObserver

object ApplicationFeature : Feature
object ApplicationInstaller : ApplicationFeatureInstaller<ApplicationFeature> {
    override fun onInstall(feature: ApplicationFeature, lifecycle: ApplicationFeatureLifecycle) {
        lifecycle.addObserver(object : ApplicationFeatureLifecycleObserver {
            override fun onApplicationResumed() {
                println("enter this line application resumed")
            }

            override fun onApplicationPaused() {
                println("enter this line application paused")
            }
        })
    }
}