package com.x930073498.lifecycle.core

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import com.x930073498.lifecycle.internal.IStateProvider
import com.x930073498.lifecycle.internal.State


interface DefaultActivityLifecycleCallback : LifecycleCallback {

    companion object : DefaultActivityLifecycleCallback {
        internal operator fun DefaultActivityLifecycleCallback.plus(callback: DefaultActivityLifecycleCallback): DefaultChainActivityLifecycleCallback {
            return DefaultChainActivityLifecycleCallback(this, callback)
        }

        internal fun DefaultActivityLifecycleCallback.wrap(state: State = if (this is IStateProvider) this.state else State()): DefaultActivityLifecycleCallbackWrapper {
            return DefaultActivityLifecycleCallbackWrapper(this, state)
        }

    }

    fun onActivityInit(activity: Activity, savedInstanceState: Bundle?) {}
    fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    fun onActivityStarted(activity: Activity) {
    }

    fun onActivityResumed(activity: Activity) {
    }

    fun onActivityPaused(activity: Activity) {
    }

    fun onActivityStopped(activity: Activity) {
    }

    fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    fun onActivityDestroyed(activity: Activity) {
    }

    fun onActivityEnd(activity: Activity){}


}

internal class DefaultActivityLifecycleCallbackWrapper(
    private val _callback: DefaultActivityLifecycleCallback,
    override val state: State = if (_callback is IStateProvider) _callback.state else State()
) : DefaultActivityLifecycleCallback, IStateProvider {
    private val callback: DefaultActivityLifecycleCallback
        get() = if (state()) _callback else DefaultActivityLifecycleCallback

    override fun onActivityInit(activity: Activity, savedInstanceState: Bundle?) {
        callback.onActivityInit(activity, savedInstanceState)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)
        callback.onActivityCreated(activity, savedInstanceState)
    }

    override fun onActivityStarted(activity: Activity) {
        callback.onActivityStarted(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        callback.onActivityResumed(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        callback.onActivityPaused(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        callback.onActivityStopped(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        callback.onActivitySaveInstanceState(activity, outState)
    }

    override fun onActivityDestroyed(activity: Activity) {
        callback.onActivityDestroyed(activity)
    }

    override fun onActivityEnd(activity: Activity) {
        callback.onActivityEnd(activity)
    }
}

internal class DefaultChainActivityLifecycleCallback(
    private val _first: DefaultActivityLifecycleCallback,
    private val _second: DefaultActivityLifecycleCallback,
    override val state: State = State()
) : DefaultActivityLifecycleCallback, IStateProvider {
    private val first: DefaultActivityLifecycleCallback
        get() = if (state()) _first else DefaultActivityLifecycleCallback
    private val second: DefaultActivityLifecycleCallback
        get() = if (state()) _second else DefaultActivityLifecycleCallback

    override fun onActivityInit(activity: Activity, savedInstanceState: Bundle?) {
        first.onActivityInit(activity, savedInstanceState)
        second.onActivityInit(activity, savedInstanceState)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        first.onActivityCreated(activity, savedInstanceState)
        second.onActivityCreated(activity, savedInstanceState)
    }

    override fun onActivityStarted(activity: Activity) {
        first.onActivityStarted(activity)
        second.onActivityStarted(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        first.onActivityResumed(activity)
        second.onActivityResumed(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        first.onActivityPaused(activity)
        second.onActivityPaused(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        first.onActivityStopped(activity)
        second.onActivityStopped(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        first.onActivitySaveInstanceState(activity, outState)
        second.onActivitySaveInstanceState(activity, outState)
    }

    override fun onActivityDestroyed(activity: Activity) {
        first.onActivityDestroyed(activity)
        second.onActivityDestroyed(activity)
    }

    override fun onActivityEnd(activity: Activity) {
        first.onActivityEnd(activity)
        second.onActivityEnd(activity)
    }
}

interface ActivityLifecycleCallback : LifecycleCallback, Application.ActivityLifecycleCallbacks,
    DefaultActivityLifecycleCallback {
    companion object : ActivityLifecycleCallback {
        internal operator fun ActivityLifecycleCallback.plus(activityLifecycleCallback: ActivityLifecycleCallback): ChainActivityLifecycleCallback {
            return ChainActivityLifecycleCallback(this, activityLifecycleCallback)
        }

        internal fun ActivityLifecycleCallback.wrap(state: State = if (this is IStateProvider) this.state else State()): ActivityLifecycleCallbackWrapper {
            return ActivityLifecycleCallbackWrapper(this, state)
        }
    }

    @RequiresApi(29)
    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)
    }

    @RequiresApi(29)
    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    @RequiresApi(29)
    override fun onActivityPreStarted(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    @RequiresApi(29)
    override fun onActivityPostStarted(activity: Activity) {
    }

    @RequiresApi(29)
    override fun onActivityPreResumed(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    @RequiresApi(29)
    override fun onActivityPostResumed(activity: Activity) {
    }

    @RequiresApi(29)
    override fun onActivityPrePaused(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    @RequiresApi(29)
    override fun onActivityPostPaused(activity: Activity) {
    }

    @RequiresApi(29)
    override fun onActivityPreStopped(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    @RequiresApi(29)
    override fun onActivityPostStopped(activity: Activity) {
    }

    @RequiresApi(29)
    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    @RequiresApi(29)
    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
    }

    @RequiresApi(29)
    override fun onActivityPreDestroyed(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    @RequiresApi(29)
    override fun onActivityPostDestroyed(activity: Activity) {
    }
}

internal class ActivityLifecycleCallbackWrapper(
    private val _callback: ActivityLifecycleCallback,
    override val state: State = if (_callback is IStateProvider) _callback.state else State()
) :
    ActivityLifecycleCallback, IStateProvider {
    private val callback: ActivityLifecycleCallback
        get() {
            return if (state()) _callback else ActivityLifecycleCallback
        }

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            super.onActivityPreCreated(activity, savedInstanceState)
            callback.onActivityPreCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)
        callback.onActivityCreated(activity, savedInstanceState)
    }

    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callback.onActivityPostCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityPreStarted(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callback.onActivityPreStarted(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        callback.onActivityStarted(activity)
    }

    override fun onActivityPostStarted(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callback.onActivityPostStarted(activity)
        }
    }

    override fun onActivityPreResumed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callback.onActivityPreResumed(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        callback.onActivityResumed(activity)
    }

    override fun onActivityPostResumed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callback.onActivityPostResumed(activity)
        }
    }

    override fun onActivityPrePaused(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callback.onActivityPrePaused(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        callback.onActivityPaused(activity)
    }

    override fun onActivityPostPaused(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callback.onActivityPostPaused(activity)
        }
    }

    override fun onActivityPreStopped(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callback.onActivityPreStopped(activity)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        callback.onActivityStopped(activity)
    }

    override fun onActivityPostStopped(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callback.onActivityPostStopped(activity)
        }
    }

    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callback.onActivityPreSaveInstanceState(activity, outState)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        callback.onActivitySaveInstanceState(activity, outState)
    }

    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callback.onActivityPostSaveInstanceState(activity, outState)
        }
    }

    override fun onActivityPreDestroyed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callback.onActivityPreDestroyed(activity)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        callback.onActivityDestroyed(activity)
    }

    override fun onActivityPostDestroyed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            callback.onActivityPostDestroyed(activity)
        }
    }

    override fun onActivityEnd(activity: Activity) {
        callback.onActivityEnd(activity)
    }
}

internal class ChainActivityLifecycleCallback(
    private val _first: ActivityLifecycleCallback,
    private val _second: ActivityLifecycleCallback,
    override val state: State = State()
) : ActivityLifecycleCallback, IStateProvider {
    private val first: ActivityLifecycleCallback
        get() = if (state()) _first else ActivityLifecycleCallback

    private val second: ActivityLifecycleCallback
        get() = if (state()) _second else ActivityLifecycleCallback

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            super.onActivityPreCreated(activity, savedInstanceState)
            first.onActivityPreCreated(activity, savedInstanceState)
            second.onActivityPreCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityCreated(activity, savedInstanceState)
        first.onActivityCreated(activity, savedInstanceState)
        second.onActivityCreated(activity, savedInstanceState)
    }

    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            first.onActivityPostCreated(activity, savedInstanceState)
            second.onActivityPostCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityPreStarted(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            first.onActivityPreStarted(activity)
            second.onActivityPreStarted(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        first.onActivityStarted(activity)
        second.onActivityStarted(activity)
    }

    override fun onActivityPostStarted(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            first.onActivityPostStarted(activity)
            second.onActivityPostStarted(activity)
        }
    }

    override fun onActivityPreResumed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            first.onActivityPreResumed(activity)
            second.onActivityPreResumed(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        first.onActivityResumed(activity)
        second.onActivityResumed(activity)
    }

    override fun onActivityPostResumed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            first.onActivityPostResumed(activity)
            second.onActivityPostResumed(activity)
        }
    }

    override fun onActivityPrePaused(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            first.onActivityPrePaused(activity)
            second.onActivityPrePaused(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        first.onActivityPaused(activity)
        second.onActivityPaused(activity)
    }

    override fun onActivityPostPaused(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            first.onActivityPostPaused(activity)
            second.onActivityPostPaused(activity)
        }
    }

    override fun onActivityPreStopped(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            first.onActivityPreStopped(activity)
            second.onActivityPreStopped(activity)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        first.onActivityStopped(activity)
        second.onActivityStopped(activity)
    }

    override fun onActivityPostStopped(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            first.onActivityPostStopped(activity)
            second.onActivityPostStopped(activity)
        }
    }

    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            first.onActivityPreSaveInstanceState(activity, outState)
            second.onActivityPreSaveInstanceState(activity, outState)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        first.onActivitySaveInstanceState(activity, outState)
        second.onActivitySaveInstanceState(activity, outState)
    }

    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            first.onActivityPostSaveInstanceState(activity, outState)
            second.onActivityPostSaveInstanceState(activity, outState)
        }
    }

    override fun onActivityPreDestroyed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            first.onActivityPreDestroyed(activity)
            second.onActivityPreDestroyed(activity)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        first.onActivityDestroyed(activity)
        second.onActivityDestroyed(activity)
    }

    override fun onActivityPostDestroyed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            first.onActivityPostDestroyed(activity)
            second.onActivityPostDestroyed(activity)
        }
    }

    override fun onActivityEnd(activity: Activity) {
        first.onActivityEnd(activity)
        second.onActivityEnd(activity)
    }
}