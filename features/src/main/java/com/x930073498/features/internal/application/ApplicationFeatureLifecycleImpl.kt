package com.x930073498.features.internal.application

import android.app.Application
import com.x930073498.features.core.application.ApplicationFeatureLifecycle
import com.x930073498.features.core.application.ApplicationFeatureLifecycleObserver
import com.x930073498.features.internal.LockList
import com.x930073498.features.internal.doOnLock
import com.x930073498.features.internal.forEach

class ApplicationFeatureLifecycleImpl() :
    ApplicationFeatureLifecycle,
    ApplicationFeatureLifecycleObserver,
    LockList<ApplicationFeatureLifecycleObserver> by LockList.create() {


    override fun addObserver(observer: ApplicationFeatureLifecycleObserver) {
        doOnLock {
            if (!contains(observer)) add(observer)
        }
    }

    override fun removeObserver(observer: ApplicationFeatureLifecycleObserver) {
        doOnLock {
            remove(observer)
        }
    }

    override fun onApplicationCreated(application:Application) {
        forEach {
            onApplicationCreated(application)
        }
    }

    override fun onApplicationStarted(application: Application) {
        forEach {
            onApplicationStarted(application)
        }
    }

    override fun onApplicationResumed(application: Application) {
        forEach {
            onApplicationResumed(application)
        }
    }

    override fun onApplicationPaused(application: Application) {
        forEach {
            onApplicationPaused(application)
        }
    }

    override fun onApplicationStopped(application: Application) {
        forEach {
            onApplicationStopped(application)
        }
    }
}