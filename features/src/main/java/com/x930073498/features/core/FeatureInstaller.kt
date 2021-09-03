package com.x930073498.features.core

 fun interface FeatureInstaller<T:Feature> {

    fun onInstall(feature:T,lifecycle: FeatureLifecycle)
}