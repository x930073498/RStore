package com.x930073498.features.core.application

interface ApplicationFeatureLifecycle {
    fun addObserver(observer: ApplicationFeatureLifecycleObserver)
    fun removeObserver(observer: ApplicationFeatureLifecycleObserver)
}