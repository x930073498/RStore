package com.x930073498.features.internal.application

import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Initializer
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

    override fun onApplicationCreated() {
        forEach {
            onApplicationCreated()
        }
    }

    override fun onApplicationStarted() {
        forEach {
            onApplicationStarted()
        }
    }

    override fun onApplicationResumed() {
        forEach {
            onApplicationResumed()
        }
    }

    override fun onApplicationPaused() {
        forEach {
            onApplicationPaused()
        }
    }

    override fun onApplicationStopped() {
        forEach {
            onApplicationStopped()
        }
    }
}