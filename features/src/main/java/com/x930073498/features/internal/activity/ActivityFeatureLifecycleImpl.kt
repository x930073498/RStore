package com.x930073498.features.internal.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Initializer
import com.x930073498.features.core.activity.ActivityFeatureLifecycle
import com.x930073498.features.core.activity.ActivityFeatureLifecycleObserver
import com.x930073498.features.core.activity.ActivityFeatureStateObserver
import com.x930073498.features.internal.LockList
import com.x930073498.features.internal.doOnLock
import com.x930073498.features.internal.forEach

internal class ActivityFeatureLifecycleImpl :
    ActivityFeatureLifecycle,
    ActivityFeatureLifecycleObserver,
    LockList<ActivityFeatureLifecycleObserver> by LockList.create() {


    override fun addObserver(observer: ActivityFeatureLifecycleObserver) {
        doOnLock {
            if (!contains(observer)) add(observer)
        }
    }

    override fun removeObserver(observer: ActivityFeatureLifecycleObserver) {
        doOnLock {
            remove(observer)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        forEach {
            onActivityCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        forEach {
            onActivityStarted(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        forEach {
            onActivityResumed(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        forEach {
            onActivityPaused(activity)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        forEach {
            onActivityStopped(activity)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        forEach {
            onActivitySaveInstanceState(activity, outState)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        forEach {
            onActivityDestroyed(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPreCreated(activity, savedInstanceState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPostCreated(activity, savedInstanceState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPreStarted(activity: Activity) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPreStarted(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPostStarted(activity: Activity) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPostStarted(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPreResumed(activity: Activity) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPreResumed(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPostResumed(activity: Activity) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPostResumed(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPrePaused(activity: Activity) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPrePaused(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPostPaused(activity: Activity) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPostPaused(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPreStopped(activity: Activity) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPreStopped(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPostStopped(activity: Activity) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPostStopped(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPreSaveInstanceState(activity, outState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPostSaveInstanceState(activity, outState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPreDestroyed(activity: Activity) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPreDestroyed(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onActivityPostDestroyed(activity: Activity) {
        forEach {
            if (this is ActivityFeatureStateObserver)
                onActivityPostDestroyed(activity)
        }
    }
}