package com.x930073498.lifecycle.internal

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.collection.arrayMapOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleObserver
import com.x930073498.lifecycle.core.*
import com.x930073498.lifecycle.core.DefaultActivityLifecycleCallback

internal fun InitializerScope.addActivityLifecycleObserverImpl(observer: LifecycleObserver): Removable {
    if (this !is InitializerScopeImpl)return Removable
    val activityMap = arrayMapOf<Activity, Removable>()
    activities.forEach {
        val removable = it.addLifecycleObserver(observer)
        activityMap[it] = removable
    }
    val removable = addLifecycleCallback(object : DefaultActivityLifecycleCallback {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (activityMap.containsKey(activity)) return
            val removable = activity.addLifecycleObserver(observer)
            activityMap[activity] = removable
        }

        override fun onActivityDestroyed(activity: Activity) {
            activityMap.remove(activity)?.remove()
        }

    })
    return Removable(application) {
        removable.remove()
        activityMap.keys.forEach {
            activityMap[it]?.remove()
        }
        activityMap.clear()
    }

}

internal fun InitializerScope.addFragmentLifecycleObserverImpl(observer: LifecycleObserver): Removable {
   if (this !is InitializerScopeImpl)return Removable
    val fragmentMap = arrayMapOf<Fragment, Removable>()
    fragments.forEach {
        val removable = it.addLifecycleObserver(observer)
        fragmentMap[it] = removable
    }
    val removable = addLifecycleCallback(object : FragmentLifecycleCallback {
        override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
            if (!fragmentMap.contains(f)) {
                val removable = f.addLifecycleObserver(observer)
                fragmentMap[f] = removable
            }
        }

        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
            fragmentMap.remove(f)?.remove()
        }
    })
    return Removable(application) {
        removable.remove()
        fragmentMap.keys.forEach {
            fragmentMap[it]?.remove()
        }
        fragmentMap.clear()
    }
}

internal fun InitializerScope.addFragmentViewLifecycleObserverImpl(observer: LifecycleObserver): Removable {
   if (this !is InitializerScopeImpl)return Removable
    val fragmentMap = arrayMapOf<Fragment, Removable>()
    fragments.forEach {
        val removable = it.addViewLifecycleObserver(observer)
        fragmentMap[it] = removable
    }
    val removable = addLifecycleCallback(object : FragmentLifecycleCallback {
        override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
            if (!fragmentMap.contains(f)) {
                val removable = f.addViewLifecycleObserver(observer)
                fragmentMap[f] = removable
            }
        }

        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
            fragmentMap.remove(f)?.remove()
        }
    })
    return Removable(application) {
        removable.remove()
        fragmentMap.keys.forEach {
            fragmentMap[it]?.remove()
        }
        fragmentMap.clear()
    }
}