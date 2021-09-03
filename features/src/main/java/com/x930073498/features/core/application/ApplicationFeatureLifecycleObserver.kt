package com.x930073498.features.core.application

interface ApplicationFeatureLifecycleObserver {
    companion object : ApplicationFeatureLifecycleObserver

    fun onApplicationCreated() {}
    fun onApplicationStarted() {}
    fun onApplicationResumed() {}
    fun onApplicationPaused() {}
    fun onApplicationStopped() {}
}