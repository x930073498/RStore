package com.x930073498.features.core.activity

interface ActivityFeatureLifecycle {

    fun addObserver(observer: ActivityFeatureLifecycleObserver)
    fun removeObserver(observer: ActivityFeatureLifecycleObserver)
}