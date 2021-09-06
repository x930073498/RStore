package com.x930073498.features.core.activity

import com.x930073498.features.core.FeatureLifecycle

interface ActivityFeatureLifecycle:FeatureLifecycle {

    fun addObserver(observer: ActivityFeatureLifecycleObserver)
    fun removeObserver(observer: ActivityFeatureLifecycleObserver)
}