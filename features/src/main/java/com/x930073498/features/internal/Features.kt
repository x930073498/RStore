package com.x930073498.features.internal

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import androidx.collection.arrayMapOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.x930073498.features.core.*
import java.util.concurrent.locks.ReentrantLock

object Features : Application.ActivityLifecycleCallbacks,
    FragmentManager.FragmentLifecycleCallbacks() {

    private val fragmentMap = mutableMapOf<Fragment, FeatureTarget.FragmentTarget>()
    private val activityMap = mutableMapOf<Activity, FeatureTarget.ActivityTarget>()
    private lateinit var application: Application
    private lateinit var applicationTarget: FeatureTarget.ApplicationTarget
    private val initializers = LockList.create<Initializer>()

    private val fragmentLock = ReentrantLock()
    private val activityLock = ReentrantLock()
    private val applicationLock = ReentrantLock()

    private fun <R> doOnFragment(action: () -> R): R {
        fragmentLock.lock()
        try {
            return action()
        } finally {
            fragmentLock.unlock()
        }
    }


    private fun <R> doOnActivity(action: () -> R): R {
        activityLock.lock()
        try {
            return action()
        } finally {
            activityLock.unlock()
        }
    }

    private fun <R> doOnApplication(action: () -> R): R {
        applicationLock.lock()
        try {
            return action()
        } finally {
            applicationLock.unlock()
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    internal fun addInitializer(initializer: Initializer): Removable {
        return if (Looper.getMainLooper() == Looper.myLooper()) {
            addInitializerInternal(initializer)
            Removable {
                remove(initializer)
            }
        } else {
            handler.post {
                addInitializerInternal(initializer)
            }
            Removable {
                handler.post {
                    remove(initializer)
                }
            }
        }
    }


    internal fun remove(initializer: Initializer) {
        initializers.doOnLock {
            remove(initializer)
        }
    }

    private fun addInitializerInternal(initializer: Initializer) {
        initializers.doOnLock {
            if (contains(initializer)) return@doOnLock
            doOnApplication {
                if (::applicationTarget.isInitialized) {
                    applicationTarget.setup(initializer)
                }
            }
            doOnActivity {
                activityMap.values.forEach {
                    it.setup(initializer)
                }
            }
            doOnFragment {
                fragmentMap.values.forEach {
                    it.setup(initializer)
                }
            }
            add(initializer)
        }
    }


    private fun setup(fragment: Fragment,fragmentManager: FragmentManager,context: Context) {
        doOnFragment {
            if (!fragmentMap.contains(fragment)) {
                FeatureTarget.requireTarget(fragment).apply {
                    if (!hasFragmentManager()){
                        this.fragmentManager=fragmentManager
                    }
                    if (!hasContext()){
                        this.context=context
                    }
                    setup(initializers)
                    fragmentMap[fragment] = this
                }
                fragment.childFragmentManager.registerFragmentLifecycleCallbacks(this, false)
            }
        }
    }

    private fun dispose(fragment: Fragment) {
        doOnFragment {
            fragmentMap[fragment]?.destroy()
            fragmentMap.remove(fragment)
        }
    }

    private fun setup(activity: Activity,savedInstanceState: Bundle?) {
        doOnActivity {
            if (!activityMap.contains(activity)) {
                if (activity !is LifecycleOwner) {
                    ReportFragment.injectIfNeededIn(activity)
                }
                FeatureTarget.requireTarget(activity).apply {
                    this.savedInstanceState=savedInstanceState
                    setup(initializers)
                    activityMap[activity] = this
                }
                if (activity is FragmentActivity) {
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(this, false)
                }

            }
        }
    }

    private fun dispose(activity: Activity) {
        doOnActivity {
            activityMap[activity]?.destroy()
            activityMap.remove(activity)
        }
    }

    internal fun setup(application: Application) {
        applicationLock.lock()
        try {
            if (!this::application.isInitialized)
                this.application = application
            if (!::applicationTarget.isInitialized) {
                applicationTarget = FeatureTarget.requireTarget(application)
                application.registerActivityLifecycleCallbacks(this)
                applicationTarget.setup(initializers)
            }


        } finally {
            applicationLock.unlock()
        }
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    private fun isAtLeastQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        setup(activity,savedInstanceState)
        FeatureTarget.requireTarget(activity).doOnActivityLifecycle {
            onActivityPreCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (!isAtLeastQ()) {
            setup(activity,savedInstanceState)
            FeatureTarget.requireTarget(activity).doOnActivityLifecycle {
                onActivityCreated(activity, savedInstanceState)
            }
        }
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
        if (!isAtLeastQ()) {
            dispose(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostDestroyed(activity: Activity) {
        dispose(activity)
    }

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        setup(f,fm,context)
        FeatureTarget.requireTarget(f).doOnFragmentLifecycle {
            onFragmentPreAttached(fm, f, context)
        }
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        dispose(f)
    }

}