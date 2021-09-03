package com.x930073498.features.core.fragment

import com.x930073498.features.core.Feature
import com.x930073498.features.core.FeatureInstaller
import com.x930073498.features.core.FeatureLifecycle

fun interface FragmentFeatureInstaller<T : Feature> : FeatureInstaller<T> {
    fun onInstall(feature: T, lifecycle: FragmentFeatureLifecycle)
    override fun onInstall(feature: T, lifecycle: FeatureLifecycle) {
        onInstall(feature, lifecycle as FragmentFeatureLifecycle)
    }
}