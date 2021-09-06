package com.x930073498.features.internal.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Initializer
import com.x930073498.features.core.activity.ActivityFeatureLifecycle
import com.x930073498.features.core.activity.ActivityFeatureLifecycleObserver
import com.x930073498.features.internal.LockList
import com.x930073498.features.internal.doOnLock
import com.x930073498.features.internal.forEach

class ActivityFeatureLifecycleImpl(
    private val target: FeatureTarget.ActivityTarget,
    initializers: LockList<Initializer>
) :
    ActivityFeatureLifecycle,
    ActivityFeatureLifecycleObserver,
    LockList<ActivityFeatureLifecycleObserver> by LockList.create() {
    init {
        initializers.forEach {
            init(target)
        }
    }

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
    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        forEach {
            onActivityPreCreated(activity, savedInstanceState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
        forEach {
            onActivityPostCreated(activity, savedInstanceState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreStarted(activity: Activity) {
        forEach {
            onActivityPreStarted(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostStarted(activity: Activity) {
        forEach {
            onActivityPostStarted(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreResumed(activity: Activity) {
        forEach {
            onActivityPreResumed(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostResumed(activity: Activity) {
        forEach {
            onActivityPostResumed(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPrePaused(activity: Activity) {
        forEach {
            onActivityPrePaused(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostPaused(activity: Activity) {
        forEach {
            onActivityPostPaused(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreStopped(activity: Activity) {
        forEach {
            onActivityPreStopped(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostStopped(activity: Activity) {
        forEach {
            onActivityPostStopped(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
        forEach {
            onActivityPreSaveInstanceState(activity, outState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        forEach {
            onActivityPostSaveInstanceState(activity, outState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreDestroyed(activity: Activity) {
        forEach {
            onActivityPreDestroyed(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostDestroyed(activity: Activity) {
        forEach {
            onActivityPostDestroyed(activity)
        }
    }
}