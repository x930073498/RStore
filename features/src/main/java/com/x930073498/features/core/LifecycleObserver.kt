package com.x930073498.features.core

import android.app.Activity
import android.app.Application
import android.content.Context
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


fun ActivityFeatureLifecycleObserver.asActivityLifecycleCallbacks(): Application.ActivityLifecycleCallbacks {
    if (this is Application.ActivityLifecycleCallbacks) return this
    return object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            this@asActivityLifecycleCallbacks.onActivityCreated(activity,savedInstanceState)
        }

        override fun onActivityStarted(activity: Activity) {
            this@asActivityLifecycleCallbacks.onActivityStarted(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            this@asActivityLifecycleCallbacks.onActivityResumed(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            this@asActivityLifecycleCallbacks.onActivityPaused(activity)
        }

        override fun onActivityStopped(activity: Activity) {
            this@asActivityLifecycleCallbacks.onActivityStopped(activity)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            this@asActivityLifecycleCallbacks.onActivitySaveInstanceState(activity,outState)
        }

        override fun onActivityDestroyed(activity: Activity) {
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
fun FragmentFeatureLifecycleObserver.asFragmentLifecycleCallbacks():FragmentManager.FragmentLifecycleCallbacks{
    if (this is FragmentManager.FragmentLifecycleCallbacks)return this
    return object :FragmentManager.FragmentLifecycleCallbacks(){
        override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
            this@asFragmentLifecycleCallbacks.onFragmentPreAttached(fm, f, context)
        }

        override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
            this@asFragmentLifecycleCallbacks.onFragmentAttached(fm, f, context)
        }

        override fun onFragmentPreCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            this@asFragmentLifecycleCallbacks.onFragmentPreCreated(fm, f, savedInstanceState)
        }

        override fun onFragmentCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            this@asFragmentLifecycleCallbacks.onFragmentCreated(fm, f, savedInstanceState)
        }

        override fun onFragmentActivityCreated(
            fm: FragmentManager,
            f: Fragment,
            savedInstanceState: Bundle?
        ) {
            this@asFragmentLifecycleCallbacks.onFragmentActivityCreated(fm, f, savedInstanceState)
        }

        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            this@asFragmentLifecycleCallbacks.onFragmentViewCreated(fm, f, v, savedInstanceState)
        }

        override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
            this@asFragmentLifecycleCallbacks.onFragmentStarted(fm, f)
        }

        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            this@asFragmentLifecycleCallbacks.onFragmentResumed(fm, f)
        }

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
            this@asFragmentLifecycleCallbacks.onFragmentPaused(fm, f)
        }

        override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
            this@asFragmentLifecycleCallbacks.onFragmentStopped(fm, f)
        }

        override fun onFragmentSaveInstanceState(
            fm: FragmentManager,
            f: Fragment,
            outState: Bundle
        ) {
            this@asFragmentLifecycleCallbacks.onFragmentSaveInstanceState(fm, f, outState)
        }

        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            this@asFragmentLifecycleCallbacks.onFragmentViewDestroyed(fm, f)
        }

        override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
            this@asFragmentLifecycleCallbacks.onFragmentDestroyed(fm, f)
        }

        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
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