package com.x930073498.features.core.application

import com.x930073498.features.core.FeatureLifecycle

interface ApplicationFeatureLifecycle:FeatureLifecycle {
    fun addObserver(observer: ApplicationFeatureLifecycleObserver)
    fun removeObserver(observer: ApplicationFeatureLifecycleObserver)
}