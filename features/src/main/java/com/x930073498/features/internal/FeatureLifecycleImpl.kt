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


    private val observers = arrayListOf<FeatureLifecycleDelegateObserver>()
    private val lock = ReentrantLock()
    private fun <R> doOnLock(action: MutableList<FeatureLifecycleDelegateObserver>.() -> R): R {
        lock.lock()
        val result = action(observers)
        lock.unlock()
        return result
    }

    private fun forEach(
        predicate: FeatureLifecycleDelegateObserver.() -> Boolean = { true },
        action: FeatureLifecycleDelegateObserver.() -> Unit
    ) {
        doOnLock {
            forEach {
                if (predicate(it)) action(it)
            }
        }
    }

    private fun forEachFragment(
        action: FragmentFeatureLifecycleObserver.() -> Unit
    ) {
        forEach({ isFragmentFeature },action)
    }

    private fun forEachActivity(action: ActivityFeatureLifecycleObserver.() -> Unit) {
        forEach({ isActivityFeature }, action)
    }

    private fun forEachApplication(action: ApplicationFeatureLifecycleObserver.() -> Unit){
        forEach({isApplicationFeature},action)
    }

    override fun addObserver(observer: FeatureLifecycleObserver) {
        doOnLock {
            add(FeatureLifecycleDelegateObserver(observer))
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
        removeObserver(observer as ActivityFeatureLifecycleObserver)
    }

    override fun removeObserver(observer: FragmentFeatureLifecycleObserver) {
        doOnLock {
            removeAll {
                it.fragmentFeatureLifecycleObserver === observer
            }
        }
    }

    override fun removeObserver(observer: ActivityFeatureLifecycleObserver) {
        doOnLock {
            removeAll {
                it.activityFeatureLifecycleObserver === observer
            }
        }
    }

    override fun removeObserver(observer: ApplicationFeatureLifecycleObserver) {
        doOnLock {
            removeAll {
                it.applicationFeatureLifecycleObserver === observer
            }
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        forEachActivity {
            onActivityCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        forEachActivity {
            onActivityStarted(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        forEachActivity { onActivityResumed(activity) }
    }

    override fun onActivityPaused(activity: Activity) {
        forEachActivity { onActivityPaused(activity) }
    }

    override fun onActivityStopped(activity: Activity) {
        forEachActivity { onActivityStopped(activity) }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        forEachActivity { onActivitySaveInstanceState(activity, outState) }
    }

    override fun onActivityDestroyed(activity: Activity) {
        forEachActivity { onActivityDestroyed(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        forEachActivity { onActivityPreCreated(activity, savedInstanceState) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
        forEachActivity { onActivityPostCreated(activity, savedInstanceState) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreStarted(activity: Activity) {
        forEachActivity { onActivityPreStarted(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostStarted(activity: Activity) {
        forEachActivity { onActivityPostStarted(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreResumed(activity: Activity) {
        forEachActivity { onActivityPreResumed(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostResumed(activity: Activity) {
        forEachActivity { onActivityPostResumed(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPrePaused(activity: Activity) {
        forEachActivity { onActivityPrePaused(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostPaused(activity: Activity) {
        forEachActivity { onActivityPostPaused(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreStopped(activity: Activity) {
        forEachActivity { onActivityPreStopped(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostStopped(activity: Activity) {
        forEachActivity { onActivityPostStopped(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
        forEachActivity { onActivityPreSaveInstanceState(activity, outState) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        forEachActivity { onActivityPostSaveInstanceState(activity, outState) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreDestroyed(activity: Activity) {
        forEachActivity { onActivityPreDestroyed(activity) }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostDestroyed(activity: Activity) {
        forEachActivity { onActivityPostDestroyed(activity) }
    }

    override fun onApplicationCreated() {
        forEachApplication {
            onApplicationCreated()
        }
    }

    override fun onApplicationStarted() {
        forEachApplication {
            onApplicationStarted()
        }
    }

    override fun onApplicationResumed() {
        forEachApplication {
            onApplicationResumed()
        }
    }

    override fun onApplicationPaused() {
        forEachApplication {
            onApplicationPaused()
        }
    }

    override fun onApplicationStopped() {
        forEachApplication {
            onApplicationStopped()
        }
    }

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        forEachFragment { onFragmentPreAttached(fm, f, context) }
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        forEachFragment { onFragmentAttached(fm, f, context) }
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        forEachFragment { onFragmentPreCreated(fm, f, savedInstanceState) }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        forEachFragment { onFragmentCreated(fm, f, savedInstanceState) }
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        forEachFragment { onFragmentActivityCreated(fm, f, savedInstanceState) }
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        forEachFragment { onFragmentViewCreated(fm, f, v, savedInstanceState) }
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        forEachFragment { onFragmentStarted(fm, f) }
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        forEachFragment { onFragmentResumed(fm, f) }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        forEachFragment { onFragmentPaused(fm, f) }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        forEachFragment { onFragmentStopped(fm, f) }
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        forEachFragment { onFragmentSaveInstanceState(fm, f, outState) }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        forEachFragment { onFragmentViewDestroyed(fm, f) }
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        forEachFragment { onFragmentDestroyed(fm, f) }
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        forEachFragment { onFragmentDetached(fm, f) }
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