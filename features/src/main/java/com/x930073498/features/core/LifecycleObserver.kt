package com.x930073498.features.core

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

sealed interface LifecycleObserver

interface ActivityFeatureLifecycleObserver : LifecycleObserver {
    companion object : ActivityFeatureLifecycleObserver

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


}


private class TargetActivityLifecycleCallbacks(
    val target: FeatureTarget.ActivityTarget,
    val callbacks: Application.ActivityLifecycleCallbacks
) : Application.ActivityLifecycleCallbacks {
    private fun isTarget(activity: Activity): Boolean {
        return activity === target.data
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (!isTarget(activity)) return
        callbacks.onActivityPreCreated(activity, savedInstanceState)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (!isTarget(activity)) return
        callbacks.onActivityCreated(activity, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (!isTarget(activity)) return
        callbacks.onActivityPostCreated(activity, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreStarted(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityPreStarted(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityStarted(activity)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostStarted(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityPostStarted(activity)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreResumed(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityPreResumed(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityResumed(activity)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostResumed(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityPostResumed(activity)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPrePaused(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityPrePaused(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityPaused(activity)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostPaused(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityPostPaused(activity)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreStopped(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityPreStopped(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityStopped(activity)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostStopped(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityPostStopped(activity)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
        if (!isTarget(activity)) return
        callbacks.onActivityPreSaveInstanceState(activity, outState)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        if (!isTarget(activity)) return
        callbacks.onActivitySaveInstanceState(activity, outState)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        if (!isTarget(activity)) return
        callbacks.onActivityPostSaveInstanceState(activity, outState)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreDestroyed(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityPreDestroyed(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityDestroyed(activity)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostDestroyed(activity: Activity) {
        if (!isTarget(activity)) return
        callbacks.onActivityPostDestroyed(activity)
    }
}

fun ActivityFeatureLifecycleObserver.asActivityLifecycleCallbacks(target: FeatureTarget.ActivityTarget): Application.ActivityLifecycleCallbacks {
    if (this is Application.ActivityLifecycleCallbacks) return TargetActivityLifecycleCallbacks(
        target,
        this
    )

    fun isTarget(activity: Activity): Boolean {
        return activity === target.data
    }
    return object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (!isTarget(activity)) return
            this@asActivityLifecycleCallbacks.onActivityCreated(activity, savedInstanceState)
        }

        override fun onActivityStarted(activity: Activity) {
            if (!isTarget(activity)) return
            this@asActivityLifecycleCallbacks.onActivityStarted(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            if (!isTarget(activity)) return
            this@asActivityLifecycleCallbacks.onActivityResumed(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            if (!isTarget(activity)) return
            this@asActivityLifecycleCallbacks.onActivityPaused(activity)
        }

        override fun onActivityStopped(activity: Activity) {
            if (!isTarget(activity)) return
            this@asActivityLifecycleCallbacks.onActivityStopped(activity)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            if (!isTarget(activity)) return
            this@asActivityLifecycleCallbacks.onActivitySaveInstanceState(activity, outState)
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (!isTarget(activity)) return
            this@asActivityLifecycleCallbacks.onActivityDestroyed(activity)
        }


    }
}

@RequiresApi(29)
interface ActivityFeatureStateObserver : ActivityFeatureLifecycleObserver,
    Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityPreStarted(activity: Activity) {

    }

    override fun onActivityPostStarted(activity: Activity) {

    }

    override fun onActivityPreResumed(activity: Activity) {

    }

    override fun onActivityPostResumed(activity: Activity) {

    }

    override fun onActivityPrePaused(activity: Activity) {

    }

    override fun onActivityPostPaused(activity: Activity) {

    }

    override fun onActivityPreStopped(activity: Activity) {

    }

    override fun onActivityPostStopped(activity: Activity) {

    }

    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityPreDestroyed(activity: Activity) {

    }

    override fun onActivityPostDestroyed(activity: Activity) {

    }
}

interface ApplicationFeatureLifecycleObserver : LifecycleObserver {
    companion object : ApplicationFeatureLifecycleObserver

    fun onApplicationCreated(application: Application) {}
    fun onApplicationStarted(application: Application) {}
    fun onApplicationResumed(application: Application) {}
    fun onApplicationPaused(application: Application) {}
    fun onApplicationStopped(application: Application) {}
}

fun FragmentFeatureLifecycleObserver.asFragmentLifecycleCallbacks(target: FeatureTarget.FragmentTarget): FragmentManager.FragmentLifecycleCallbacks {
    if (this is FragmentManager.FragmentLifecycleCallbacks) return this

    fun isTarget(f: Fragment): Boolean {
        return f === target.data
    }
    return object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentPreAttached(fm, f, context)
        }

        override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentAttached(fm, f, context)
        }

        override fun onFragmentPreCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentPreCreated(fm, f, savedInstanceState)
        }

        override fun onFragmentCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentCreated(fm, f, savedInstanceState)
        }

        override fun onFragmentActivityCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentActivityCreated(fm, f, savedInstanceState)
        }

        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentViewCreated(fm, f, v, savedInstanceState)
        }

        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentStarted(fm, f)
        }

        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentResumed(fm, f)
        }

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentPaused(fm, f)
        }

        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentStopped(fm, f)
        }

        override fun onFragmentSaveInstanceState(
            fm: FragmentManager,
            f: Fragment,
            outState: Bundle
        ) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentSaveInstanceState(fm, f, outState)
        }

        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentViewDestroyed(fm, f)
        }

        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentDestroyed(fm, f)
        }

        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
            if (!isTarget(f)) return
            this@asFragmentLifecycleCallbacks.onFragmentDetached(fm, f)
        }
    }
}

interface FragmentFeatureLifecycleObserver : LifecycleObserver {
    companion object : FragmentFeatureLifecycleObserver

    fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
    }

    fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
    }

    fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
    }

    fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
    }

    fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
    }

    fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
    }

    fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
    }

    fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
    }
}