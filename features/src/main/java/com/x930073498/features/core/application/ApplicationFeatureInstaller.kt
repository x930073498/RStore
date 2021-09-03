package com.x930073498.features.core.application

import com.x930073498.features.core.Feature
import com.x930073498.features.core.FeatureInstaller
import com.x930073498.features.core.FeatureLifecycle
import com.x930073498.features.core.activity.ActivityFeatureLifecycle

fun interface ApplicationFeatureInstaller<T : Feature> : FeatureInstaller<T> {
    fun onInstall(feature: T, lifecycle: ApplicationFeatureLifecycle)
    override fun onInstall(feature: T, lifecycle: FeatureLifecycle) {
        onInstall(feature, lifecycle as ApplicationFeatureLifecycle)
    }
}