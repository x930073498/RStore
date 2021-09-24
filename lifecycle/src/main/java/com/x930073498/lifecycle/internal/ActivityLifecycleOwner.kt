package com.x930073498.lifecycle.internal

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class ActivityLifecycleOwner internal constructor(val activity: Activity, val fragment: ReportFragment) :
    LifecycleOwner {
    private val mLifecycleRegistry = LifecycleRegistry(this)

    internal companion object {
        private val map = mutableMapOf<Activity, ActivityLifecycleOwner>()

        @JvmStatic
        fun get(activity: Activity): ActivityLifecycleOwner? {
            return map[activity]
        }

        fun require(activity: Activity): ActivityLifecycleOwner {
            return map[activity] ?: throw LifecycleException("activity[${activity}] has destroyed")
        }

        @JvmStatic
        fun put(owner: ActivityLifecycleOwner) {
            map[owner.activity] = owner
        }
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    init {
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    map.remove(activity)
                }
            }

        })
    }
}