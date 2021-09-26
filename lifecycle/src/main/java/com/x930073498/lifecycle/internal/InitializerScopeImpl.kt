package com.x930073498.lifecycle.internal

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.x930073498.lifecycle.core.*
import com.x930073498.lifecycle.core.ActivityLifecycleCallback.Companion.plus
import com.x930073498.lifecycle.core.ActivityLifecycleCallback.Companion.wrap
import com.x930073498.lifecycle.core.ApplicationLifecycleCallback.Companion.asObserver
import com.x930073498.lifecycle.core.ApplicationLifecycleCallback.Companion.plus
import com.x930073498.lifecycle.core.ApplicationLifecycleCallback.Companion.wrap
import com.x930073498.lifecycle.core.DefaultActivityLifecycleCallback.Companion.plus
import com.x930073498.lifecycle.core.DefaultActivityLifecycleCallback.Companion.wrap
import com.x930073498.lifecycle.core.FragmentLifecycleCallback.Companion.plus
import com.x930073498.lifecycle.core.FragmentLifecycleCallback.Companion.wrap

internal class InitializerScopeImpl : Application.ActivityLifecycleCallbacks,
    FragmentManager.FragmentLifecycleCallbacks(), InitializerScope, LifecycleEventObserver {

    private val environment = ScopeEnvironmentImpl(
        InitializerScopeImpl.application,
        InitializerScopeImpl.activities,
        InitializerScopeImpl.fragments,
        ProcessLifecycleOwner.get()
    )

    private val _activities = arrayListOf<Activity>()
    override val activities: List<Activity> = environment.activities
    override val fragments: List<Fragment> = environment.fragments
    override val processLifecycleOwner: LifecycleOwner = environment.processLifecycleOwner


     fun Removable(activity: Activity, action: () -> Unit): Removable {
        return environment.Removable(activity, action)
    }

     fun Removable(fragment: Fragment, action: () -> Unit): Removable {
        return environment.Removable(fragment, action)
    }

     fun Removable(application: Application, action: () -> Unit): Removable {
        return environment.Removable(application, action)
    }

    override val application: Application by environment::application
    private var activityLifecycleCallback: ActivityLifecycleCallback = ActivityLifecycleCallback
    private var defaultActivityLifecycleCallback: DefaultActivityLifecycleCallback =
        DefaultActivityLifecycleCallback

    private var isRemoved = false
    private var fragmentLifecycleCallback: FragmentLifecycleCallback = FragmentLifecycleCallback

    private var applicationLifecycleCallback: ApplicationLifecycleCallback =
        ApplicationLifecycleCallback



    internal fun callInit() {
        application.registerActivityLifecycleCallbacks(this)
        processLifecycleOwner.lifecycle.addObserver(this)
    }

    internal companion object {
        private lateinit var application: Application
        private val activities = arrayListOf<Activity>()
        private val fragments = arrayListOf<Fragment>()
        internal fun setup(application: Application) {
            if (!this::application.isInitialized) {
                this.application = application
                val fragmentLifecycleCallbacks =
                    object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentPreAttached(
                            fm: FragmentManager,
                            f: Fragment,
                            context: Context
                        ) {
                            fragments.add(f)
                        }

                        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
                            fragments.remove(f)
                        }
                    }

                fun registerFragmentCallback(activity: Activity) {
                    if (activity is FragmentActivity) {
                        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                            fragmentLifecycleCallbacks,
                            true
                        )
                    }
                }

                fun unregisterFragmentCallback(activity: Activity) {
                    if (activity is FragmentActivity) {
                        activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(
                            fragmentLifecycleCallbacks
                        )
                    }
                }

                application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallback {
                    override fun onActivityPreCreated(
                        activity: Activity,
                        savedInstanceState: Bundle?
                    ) {
                        if (!activities.contains(activity)) {
                            activities.add(activity)
                            registerFragmentCallback(activity)
                        }
                    }

                    override fun onActivityCreated(
                        activity: Activity,
                        savedInstanceState: Bundle?
                    ) {
                        if (!activities.contains(activity)) {
                            activities.add(activity)
                            registerFragmentCallback(activity)
                        }
                    }

                    override fun onActivityDestroyed(activity: Activity) {
                        activities.remove(activity)
                        unregisterFragmentCallback(activity)
                    }
                })
            }
        }
    }


    override fun addLifecycleCallback(lifecycleCallback: LifecycleCallback): Removable {
        if (isRemoved) return Removable
        return when (lifecycleCallback) {
            is ActivityLifecycleCallback -> {
                val wrapper = lifecycleCallback.wrap()
                activityLifecycleCallback += wrapper
                wrapper.asRemovable()
            }
            is ApplicationLifecycleCallback -> {
                val wrapper = lifecycleCallback.wrap()
                applicationLifecycleCallback += wrapper
                wrapper.asRemovable()
            }

            is FragmentLifecycleCallback -> {
                val wrapper = lifecycleCallback.wrap()
                fragmentLifecycleCallback += wrapper
                wrapper.asRemovable()
            }
            is DefaultActivityLifecycleCallback -> {
                val wrapper = lifecycleCallback.wrap()
                defaultActivityLifecycleCallback += wrapper
                wrapper.asRemovable()
            }
        }
    }

    override fun addApplicationLifecycleObserver(observer: LifecycleObserver): Removable {
        if (isRemoved) return Removable
        processLifecycleOwner.lifecycle.addObserver(observer)
        return Removable(application) {
            processLifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    override fun Activity.addLifecycleObserver(observer: LifecycleObserver): Removable {
        if (isDestroyed || isFinishing || isRemoved) return Removable
        if (this is LifecycleOwner) {
            if (lifecycle.currentState < Lifecycle.State.INITIALIZED) return Removable
            lifecycle.addObserver(observer)
            return Removable(this) {
                lifecycle.removeObserver(observer)
            }
        } else {
            var owner = ActivityLifecycleOwner.get(this)
            if (owner == null) {
                ReportFragment.injectIfNeededIn(this)
                owner = ActivityLifecycleOwner.get(this)
            }
            val lifecycle = owner?.lifecycle ?: return Removable
            if (lifecycle.currentState < Lifecycle.State.INITIALIZED) return Removable
            lifecycle.addObserver(observer)
            return Removable(this) {
                lifecycle.removeObserver(observer)
            }
        }
    }

    override fun Fragment.addLifecycleObserver(observer: LifecycleObserver): Removable {
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED) || isRemoved) {
            return Removable
        }
        lifecycle.addObserver(observer)
        return Removable(this) {
            lifecycle.removeObserver(observer)
        }
    }

    override fun Fragment.addViewLifecycleObserver(observer: LifecycleObserver): Removable {
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED) || isRemoved) {
            return Removable
        }
        val owner = viewLifecycleOwnerLiveData.value
        return if (owner != null) {
            val lifecycle = owner.lifecycle
            if (lifecycle.currentState < Lifecycle.State.INITIALIZED || isRemoved) {
                return Removable
            }
            lifecycle.addObserver(observer)
            Removable(this) {
                owner.lifecycle.removeObserver(observer)
            }
        } else {
            val livedataObserver = viewLifecycleOwnerLiveData.observe(this) {
                it.lifecycle.addObserver(observer)
            }
            Removable(this) {
                viewLifecycleOwnerLiveData.value?.lifecycle?.removeObserver(observer)
                viewLifecycleOwnerLiveData.removeObserver(livedataObserver)
            }
        }
    }

    override fun remove() {
        isRemoved = true
        activityLifecycleCallback = ActivityLifecycleCallback
        fragmentLifecycleCallback = FragmentLifecycleCallback
        applicationLifecycleCallback = ApplicationLifecycleCallback
        application.unregisterActivityLifecycleCallbacks(this)
        _activities.forEach {
            if (it is FragmentActivity) {
                it.supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
            }
        }
        _activities.clear()
        environment.clearRemovable()
    }


    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityInit(activity, savedInstanceState)
            defaultActivityLifecycleCallback.onActivityInit(activity, savedInstanceState)
            activityLifecycleCallback.onActivityPreCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity !is LifecycleOwner) {
            if (ActivityLifecycleOwner.get(activity) == null)
                ReportFragment.injectIfNeededIn(activity)
        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(this, true)
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityInit(activity, savedInstanceState)
            defaultActivityLifecycleCallback.onActivityInit(activity, savedInstanceState)
        }
        activityLifecycleCallback.onActivityCreated(activity, savedInstanceState)
        defaultActivityLifecycleCallback.onActivityCreated(activity, savedInstanceState)

    }

    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityPostCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityPreStarted(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityPreStarted(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        activityLifecycleCallback.onActivityStarted(activity)
        defaultActivityLifecycleCallback.onActivityStarted(activity)
    }

    override fun onActivityPostStarted(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityPostStarted(activity)
        }
    }

    override fun onActivityPreResumed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityPreResumed(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        activityLifecycleCallback.onActivityResumed(activity)
        defaultActivityLifecycleCallback.onActivityResumed(activity)
    }

    override fun onActivityPostResumed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityPostResumed(activity)
        }
    }

    override fun onActivityPrePaused(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityPrePaused(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        activityLifecycleCallback.onActivityPaused(activity)
        defaultActivityLifecycleCallback.onActivityPaused(activity)
    }

    override fun onActivityPostPaused(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityPostPaused(activity)
        }
    }

    override fun onActivityPreStopped(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityPreStopped(activity)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        activityLifecycleCallback.onActivityStopped(activity)
        defaultActivityLifecycleCallback.onActivityStopped(activity)
    }

    override fun onActivityPostStopped(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityPostStopped(activity)
        }
    }

    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityPreSaveInstanceState(activity, outState)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        activityLifecycleCallback.onActivitySaveInstanceState(activity, outState)
        defaultActivityLifecycleCallback.onActivitySaveInstanceState(activity, outState)
    }

    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityPostSaveInstanceState(activity, outState)
        }
    }

    override fun onActivityPreDestroyed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activityLifecycleCallback.onActivityPreDestroyed(activity)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityLifecycleCallback.onActivityDestroyed(activity)
        defaultActivityLifecycleCallback.onActivityDestroyed(activity)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (activity is FragmentActivity) {
                activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
            }
            defaultActivityLifecycleCallback.onActivityEnd(activity)
            activityLifecycleCallback.onActivityEnd(activity)
            environment.disposeRemovable(activity)
        }
    }

    override fun onActivityPostDestroyed(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (activity is FragmentActivity) {
                activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
            }
            activityLifecycleCallback.onActivityPostDestroyed(activity)
            activityLifecycleCallback.onActivityEnd(activity)
            defaultActivityLifecycleCallback.onActivityEnd(activity)
            environment.disposeRemovable(activity)
        }
    }

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        fragmentLifecycleCallback.onFragmentPreAttached(fm, f, context)
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        fragmentLifecycleCallback.onFragmentAttached(fm, f, context)
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        fragmentLifecycleCallback.onFragmentPreCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        fragmentLifecycleCallback.onFragmentCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        fragmentLifecycleCallback.onFragmentActivityCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        fragmentLifecycleCallback.onFragmentViewCreated(fm, f, v, savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        fragmentLifecycleCallback.onFragmentStarted(fm, f)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        fragmentLifecycleCallback.onFragmentResumed(fm, f)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        fragmentLifecycleCallback.onFragmentPaused(fm, f)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        fragmentLifecycleCallback.onFragmentStopped(fm, f)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        fragmentLifecycleCallback.onFragmentSaveInstanceState(fm, f, outState)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        fragmentLifecycleCallback.onFragmentViewDestroyed(fm, f)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        fragmentLifecycleCallback.onFragmentDestroyed(fm, f)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        fragmentLifecycleCallback.onFragmentDetached(fm, f)
        environment.disposeRemovable(f)

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {

            Lifecycle.Event.ON_START -> applicationLifecycleCallback.onApplicationStarted(
                application
            )
            Lifecycle.Event.ON_RESUME -> applicationLifecycleCallback.onApplicationResumed(
                application
            )
            Lifecycle.Event.ON_PAUSE -> applicationLifecycleCallback.onApplicationPaused(application)
            Lifecycle.Event.ON_STOP -> applicationLifecycleCallback.onApplicationStopped(application)
            else -> {
            }

        }
    }
}