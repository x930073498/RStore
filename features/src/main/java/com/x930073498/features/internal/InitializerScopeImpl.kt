package com.x930073498.features.internal

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.lifecycle.LifecycleObserver
import com.x930073498.features.app
import com.x930073498.features.core.*
import com.x930073498.features.lifecycleOwner


internal object DEFAULT : InitializerScope {

    override fun destroy() {

    }

    override fun FeatureTarget.FragmentTarget.addObserver(observer: FragmentFeatureLifecycleObserver) =
        Removable

    override fun FeatureTarget.FragmentTarget.addObserver(
        observer: LifecycleObserver,
        boolean: Boolean
    ) = Removable

    override fun FeatureTarget.ActivityTarget.addObserver(observer: ActivityFeatureLifecycleObserver) =
        Removable

    override fun FeatureTarget.ActivityTarget.addObserver(observer: ActivityFeatureStateObserver) =
        Removable

    override fun FeatureTarget.ActivityTarget.addObserver(observer: LifecycleObserver) = Removable

    override fun FeatureTarget.ApplicationTarget.addObserver(observer: ApplicationFeatureLifecycleObserver) =
        Removable

    override fun FeatureTarget.ApplicationTarget.addObserver(observer: LifecycleObserver) =
        Removable

}

internal class InitializerScopeImpl(private val target: FeatureTarget) : InitializerScope {

    private val removableList = LockList.create<Removable>()
    private val activityCallbacks = LockList.create<Application.ActivityLifecycleCallbacks>()
    private val fragmentCallbacks = LockList.create<FragmentManager.FragmentLifecycleCallbacks>()


    private fun Removable(action: () -> Unit): Removable {
        return com.x930073498.features.core.Removable {
            action()
        }.apply {
            removableList.doOnLock {
                add(this@apply)
            }
        }
    }

    internal fun doOnActivityLifecycle(action: Application.ActivityLifecycleCallbacks.() -> Unit) {
        activityCallbacks.forEach(action)
    }

    internal fun doOnFragmentLifecycle(action: FragmentManager.FragmentLifecycleCallbacks.() -> Unit) {
        fragmentCallbacks.forEach(action)
    }


    internal fun Initializer.setup() {
        setup(target)
    }


    override fun destroy() {
        removableList.forEach {
            remove()
        }
        removableList.doOnLock {
            clear()
        }
        activityCallbacks.doOnLock {
            clear()
        }
        fragmentCallbacks.doOnLock {
            clear()
        }
    }

    override fun FeatureTarget.FragmentTarget.addObserver(observer: FragmentFeatureLifecycleObserver): Removable {
        val callback = observer.asFragmentLifecycleCallbacks(this)
        fragmentManager.registerFragmentLifecycleCallbacks(callback, false)
        fragmentCallbacks.doOnLock {
            add(callback)
        }
        return Removable {
            fragmentManager.unregisterFragmentLifecycleCallbacks(callback)
            fragmentCallbacks.doOnLock { remove(callback) }
        }

    }

    override fun FeatureTarget.FragmentTarget.addObserver(
        observer: LifecycleObserver,
        bindView: Boolean
    ): Removable {
        if (bindView) {
            val owner = data.viewLifecycleOwnerLiveData.value
            return if (owner != null) {
                owner.lifecycle.addObserver(observer)
                Removable {
                    owner.lifecycle.removeObserver(observer)
                }
            } else {
                data.viewLifecycleOwnerLiveData.observe(data) {
                    it?.lifecycle?.addObserver(observer)
                }
                Removable {
                    data.viewLifecycleOwnerLiveData.value?.lifecycle?.removeObserver(observer)
                }
            }
        } else {
            data.lifecycle.addObserver(observer)
            return Removable {
                data.lifecycle.removeObserver(observer)
            }
        }
    }

    override fun FeatureTarget.ActivityTarget.addObserver(observer: ActivityFeatureLifecycleObserver): Removable {
        val callback = observer.asActivityLifecycleCallbacks(this)
        activityCallbacks.doOnLock {
            add(callback)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            data.registerActivityLifecycleCallbacks(callback)
            return Removable {
                data.unregisterActivityLifecycleCallbacks(callback)
                activityCallbacks.doOnLock {
                    remove(callback)
                }
            }
        }
        data.application.registerActivityLifecycleCallbacks(callback)
        return Removable {
            data.application.unregisterActivityLifecycleCallbacks(callback)
            activityCallbacks.doOnLock {
                remove(callback)
            }
        }

    }

    @RequiresApi(29)
    override fun FeatureTarget.ActivityTarget.addObserver(observer: ActivityFeatureStateObserver): Removable {
      val callback=observer.asActivityLifecycleCallbacks(this)
        data.registerActivityLifecycleCallbacks(callback)
        activityCallbacks.doOnLock {
            add(callback)
        }
        return Removable {
            data.unregisterActivityLifecycleCallbacks(callback)
            activityCallbacks.doOnLock {
                remove(callback)
            }
        }
    }

    override fun FeatureTarget.ActivityTarget.addObserver(observer: LifecycleObserver): Removable {
        data.lifecycleOwner.lifecycle.addObserver(observer)
        return Removable {
            try {
                data.lifecycleOwner.lifecycle.removeObserver(observer)
            } catch (e: FeaturesException) {

            }
        }
    }

    override fun FeatureTarget.ApplicationTarget.addObserver(observer: ApplicationFeatureLifecycleObserver): Removable {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> observer.onApplicationCreated(data)
                Lifecycle.Event.ON_START -> observer.onApplicationStarted(data)
                Lifecycle.Event.ON_RESUME -> observer.onApplicationResumed(data)
                Lifecycle.Event.ON_PAUSE -> observer.onApplicationPaused(data)
                Lifecycle.Event.ON_STOP -> observer.onApplicationStopped(data)
                Lifecycle.Event.ON_DESTROY -> {
                }
                Lifecycle.Event.ON_ANY -> {
                }
            }

        }
        return addObserver(lifecycleObserver)
    }

    override fun FeatureTarget.ApplicationTarget.addObserver(observer: LifecycleObserver): Removable {
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
        return Removable {
            ProcessLifecycleOwner.get().lifecycle.removeObserver(observer)
        }
    }
}