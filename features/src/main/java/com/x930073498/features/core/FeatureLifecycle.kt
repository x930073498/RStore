package com.x930073498.features.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.x930073498.features.core.activity.ActivityFeatureLifecycle
import com.x930073498.features.core.activity.ActivityFeatureLifecycleObserver
import com.x930073498.features.core.application.ApplicationFeatureLifecycle
import com.x930073498.features.core.application.ApplicationFeatureLifecycleObserver
import com.x930073498.features.core.fragment.FragmentFeatureLifecycle
import com.x930073498.features.core.fragment.FragmentFeatureLifecycleObserver

interface FeatureLifecycle {
    companion object : FeatureLifecycle
}


fun FeatureLifecycle.addLifecycleObserver(observer: LifecycleObserver) {
    when (this) {
        is ApplicationFeatureLifecycle -> {
            addObserver(object : ApplicationFeatureLifecycleObserver {
                override fun onApplicationCreated(application: Application) {
                    observer.onStateChange(LifecycleObserver.State.ON_CREATE)
                }


                override fun onApplicationStarted(application: Application) {
                    observer.onStateChange(LifecycleObserver.State.ON_START)
                }

                override fun onApplicationResumed(application: Application) {
                    observer.onStateChange(LifecycleObserver.State.ON_RESUME)
                }

                override fun onApplicationPaused(application: Application) {
                    observer.onStateChange(LifecycleObserver.State.ON_PAUSE)
                }

                override fun onApplicationStopped(application: Application) {
                    observer.onStateChange(LifecycleObserver.State.ON_STOP)
                }
            })
        }
        is ActivityFeatureLifecycle -> {
            addObserver(object : ActivityFeatureLifecycleObserver {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    observer.onStateChange(LifecycleObserver.State.ON_CREATE)
                }

                override fun onActivityResumed(activity: Activity) {
                    observer.onStateChange(LifecycleObserver.State.ON_RESUME)
                }

                override fun onActivityStarted(activity: Activity) {
                    observer.onStateChange(LifecycleObserver.State.ON_START)
                }

                override fun onActivityStopped(activity: Activity) {
                    observer.onStateChange(LifecycleObserver.State.ON_STOP)
                }

                override fun onActivityPaused(activity: Activity) {
                    observer.onStateChange(LifecycleObserver.State.ON_PAUSE)
                }
            })
        }

        is FragmentFeatureLifecycle -> {
            addObserver(object : FragmentFeatureLifecycleObserver {
                override fun onFragmentCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    savedInstanceState: Bundle?
                ) {
                    observer.onStateChange(LifecycleObserver.State.ON_CREATE)
                }

                override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                    observer.onStateChange(LifecycleObserver.State.ON_RESUME)
                }

                override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
                    observer.onStateChange(LifecycleObserver.State.ON_START)
                }

                override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
                    observer.onStateChange(LifecycleObserver.State.ON_PAUSE)
                }

                override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
                    observer.onStateChange(LifecycleObserver.State.ON_STOP)
                }
            })
        }

    }
}


fun FeatureLifecycle.doOnState(mState: LifecycleObserver.State, action: () -> Unit) {
    addLifecycleObserver(object : LifecycleObserver {
        override fun onStateChange(state: LifecycleObserver.State) {
            if (state == mState) {
                action()
            }
        }
    })
}


