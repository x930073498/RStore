package com.x930073498.features.internal

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.x930073498.features.core.FeatureLifecycleObserver
import com.x930073498.features.core.application.ApplicationFeatureLifecycle
import com.x930073498.features.core.application.ApplicationFeatureLifecycleObserver
import java.util.concurrent.locks.ReentrantLock

internal object FeatureAgent : ApplicationFeatureLifecycle, LifecycleEventObserver {
    private val map = mutableMapOf<Any, FeatureLifecycleImpl>()
    private val lock = ReentrantLock()
    private val applicationObservers = arrayListOf<ApplicationFeatureLifecycleObserver>()

    private fun <R> doOnLock(action: MutableMap<Any, FeatureLifecycleImpl>.() -> R): R {
        lock.lock()
        val result = action(map)
        lock.unlock()
        return result
    }


    fun doOnLifecycle(component: Any, action: FeatureLifecycleObserver.() -> Unit) {
        doOnLock {
            map[component]?.let {
                action(it)
            }
        }
    }

    private fun createFeatureLifecycle(component: Any): FeatureLifecycleImpl {
        val lifecycle = FeatureLifecycleImpl()
        addObserver(lifecycle)
        FeatureRepository.runFeatureInstall(component, lifecycle)
        return lifecycle
    }

    fun setup(fragment: Fragment) {
        doOnLock {
            if (!contains(fragment)) {
                put(fragment, createFeatureLifecycle(fragment))
            }
        }

    }

    fun setup(
        activity: Activity,
        fragmentLifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks
    ) {
        doOnLock {
            if (!contains(activity)) {
                put(activity, createFeatureLifecycle(activity))
                if (activity is FragmentActivity) {
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                        fragmentLifecycleCallbacks,
                        true
                    )
                }
            }
        }
    }

    fun dispose(fragment: Fragment) {
        doOnLock {
            val lifecycle = remove(fragment) ?: return@doOnLock
            removeObserver(lifecycle)
        }
    }

    fun dispose(
        activity: Activity,
        fragmentLifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks
    ) {
        doOnLock {
            val lifecycle = remove(activity)
            if (activity is FragmentActivity) {
                activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(
                    fragmentLifecycleCallbacks
                )
            }
            if (lifecycle != null) {
                removeObserver(lifecycle)
            }

        }
    }

    override fun addObserver(observer: ApplicationFeatureLifecycleObserver) {
        applicationObservers.add(observer)
    }

    override fun removeObserver(observer: ApplicationFeatureLifecycleObserver) {
        applicationObservers.remove(observer)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> applicationObservers.forEach {
                it.onApplicationCreated()
            }
            Lifecycle.Event.ON_START -> applicationObservers.forEach {
                it.onApplicationStarted()
            }
            Lifecycle.Event.ON_RESUME -> applicationObservers.forEach {
                it.onApplicationResumed()
            }
            Lifecycle.Event.ON_PAUSE -> applicationObservers.forEach {
                it.onApplicationPaused()
            }
            Lifecycle.Event.ON_STOP -> applicationObservers.forEach {
                it.onApplicationStopped()
            }
            Lifecycle.Event.ON_DESTROY -> {
            }
            Lifecycle.Event.ON_ANY -> {
            }
        }
    }


}