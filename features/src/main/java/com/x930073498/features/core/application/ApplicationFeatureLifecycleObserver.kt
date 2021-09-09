package com.x930073498.features.core.application

import android.app.Application

interface ApplicationFeatureLifecycleObserver {
    companion object : ApplicationFeatureLifecycleObserver

    fun onApplicationCreated(application:Application) {}
    fun onApplicationStarted(application: Application) {}
    fun onApplicationResumed(application: Application) {}
    fun onApplicationPaused(application: Application) {}
    fun onApplicationStopped(application: Application) {}
}