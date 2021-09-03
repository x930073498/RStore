package com.x930073498.features.internal

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.x930073498.features.core.FeatureLifecycle
import com.x930073498.features.core.FeatureLifecycleObserver
import com.x930073498.features.core.activity.ActivityFeatureLifecycleObserver
import com.x930073498.features.core.application.ApplicationFeatureLifecycle
import com.x930073498.features.core.application.ApplicationFeatureLifecycleObserver
import com.x930073498.features.core.fragment.FragmentFeatureLifecycle
import com.x930073498.features.core.fragment.FragmentFeatureLifecycleObserver
import java.lang.IllegalArgumentException
import java.util.concurrent.locks.ReentrantLock

class FeatureLifecycleImpl() : FeatureLifecycle,
    FeatureLifecycleObserver, LifecycleEventObserver {


    private val observers = arrayListOf<FeatureLifecycleObserver>()
    private val lock = ReentrantLock()
    private fun <R> doOnLock(action: MutableList<FeatureLifecycleObserver>.() -> R): R {
        lock.lock()
        val result = action(observers)
        lock.unlock()
        return result
    }

    private fun forEach(action: FeatureLifecycleObserver.() -> Unit) {
        doOnLock {
            forEach(action)
        }
    }

    override fun addObserver(observer: FeatureLifecycleObserver) {
        doOnLock {
            add(observer)
        }
    }

    override fun addObserver(observer: FragmentFeatureLifecycleObserver) {
        addObserver(FeatureLifecycleDelegateObserver(observer))
    }

    override fun addObserver(observer: ActivityFeatureLifecycleObserver) {
        addObserver(FeatureLifecycleDelegateObserver(activityFeatureLifecycleObserver = observer))
    }

    override fun addObserver(observer: ApplicationFeatureLifecycleObserver) {
        addObserver(FeatureLifecycleDelegateObserver(applicationFeatureLifecycleObserver = observer))
    }

    override fun removeObserver(observer: FeatureLifecycleObserver) {
        doOnLock {
            remove(observer)
        }
    }

    override fun removeObserver(observer: FragmentFeatureLifecycleObserver) {
        doOnLock {
            removeAll {
                (it is FeatureLifecycleDelegateObserver && it.fragmentFeatureLifecycleObserver === observer) || (it === observer)
            }
        }
    }

    override fun removeObserver(observer: ActivityFeatureLifecycleObserver) {
        doOnLock {
            removeAll {
                (it is FeatureLifecycleDelegateObserver && it.activityFeatureLifecycleObserver === observer) || (it === observer)
            }
        }
    }

    override fun removeObserver(observer: ApplicationFeatureLifecycleObserver) {
        doOnLock {
            removeAll {
                (it is FeatureLifecycleDelegateObserver && it.applicationFeatureLifecycleObserver === observer) || (it === observer)
            }
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
        forEach { onActivityResumed(activity) }
    }

    override fun onActivityPaused(activity: Activity) {
        forEach { onActivityPaused(activity) }
    }

    override fun onActivityStopped(activity: Activity) {
        forEach { onActivityStopped(activity) }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        forEach { onActivitySaveInstanceState(activity, outState) }
    }

    override fun onActivityDestroyed(activity: Activity) {
        forEach { onActivityDestroyed(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        forEach { onActivityPreCreated(activity, savedInstanceState) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
        forEach { onActivityPostCreated(activity, savedInstanceState) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreStarted(activity: Activity) {
        forEach { onActivityPreStarted(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostStarted(activity: Activity) {
        forEach { onActivityPostStarted(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreResumed(activity: Activity) {
        forEach { onActivityPreResumed(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostResumed(activity: Activity) {
        forEach { onActivityPostResumed(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPrePaused(activity: Activity) {
        forEach { onActivityPrePaused(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostPaused(activity: Activity) {
        forEach { onActivityPostPaused(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreStopped(activity: Activity) {
        forEach { onActivityPreStopped(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostStopped(activity: Activity) {
        forEach { onActivityPostStopped(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
        forEach { onActivityPreSaveInstanceState(activity, outState) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        forEach { onActivityPostSaveInstanceState(activity, outState) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreDestroyed(activity: Activity) {
        forEach { onActivityPreDestroyed(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostDestroyed(activity: Activity) {
        forEach { onActivityPostDestroyed(activity) }
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

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        forEach { onFragmentPreAttached(fm, f, context) }
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        forEach { onFragmentAttached(fm, f, context) }
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        forEach { onFragmentPreCreated(fm, f, savedInstanceState) }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        forEach { onFragmentCreated(fm, f, savedInstanceState) }
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        forEach { onFragmentActivityCreated(fm, f, savedInstanceState) }
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        forEach { onFragmentViewCreated(fm, f, v, savedInstanceState) }
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        forEach { onFragmentStarted(fm, f) }
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        forEach { onFragmentResumed(fm, f) }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        forEach { onFragmentPaused(fm, f) }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        forEach { onFragmentStopped(fm, f) }
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        forEach { onFragmentSaveInstanceState(fm, f, outState) }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        forEach { onFragmentViewDestroyed(fm, f) }
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        forEach { onFragmentDestroyed(fm, f) }
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        forEach { onFragmentDetached(fm, f) }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                onApplicationCreated()
            }
            Lifecycle.Event.ON_START -> {
                onApplicationStarted()
            }
            Lifecycle.Event.ON_RESUME -> {
                onApplicationResumed()
            }
            Lifecycle.Event.ON_PAUSE -> {
                onApplicationPaused()
            }
            Lifecycle.Event.ON_STOP -> {
                onApplicationStopped()
            }

            Lifecycle.Event.ON_DESTROY -> {

            }
            Lifecycle.Event.ON_ANY -> throw IllegalArgumentException("ON_ANY must not been send by anybody")
        }

    }
}