package com.x930073498.features.core.activity

import com.x930073498.features.core.Feature
import com.x930073498.features.core.FeatureInstaller
import com.x930073498.features.core.FeatureLifecycle

fun interface ActivityFeatureInstaller<T : Feature> : FeatureInstaller<T> {
    fun onInstall(feature: T, lifecycle: ActivityFeatureLifecycle)
    override fun onInstall(feature: T, lifecycle: FeatureLifecycle) {
        onInstall(feature, lifecycle as ActivityFeatureLifecycle)
    }
}