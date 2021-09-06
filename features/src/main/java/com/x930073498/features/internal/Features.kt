package com.x930073498.features.internal

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
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
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Initializer
import com.x930073498.features.core.activity.ActivityFeatureLifecycleObserver
import com.x930073498.features.core.application.ApplicationFeatureLifecycleObserver
import com.x930073498.features.core.fragment.FragmentFeatureLifecycleObserver
import java.util.concurrent.locks.ReentrantLock

object Features : Application.ActivityLifecycleCallbacks,
    FragmentManager.FragmentLifecycleCallbacks(), LifecycleEventObserver {
    private val fragmentMap = arrayMapOf<Fragment, FeatureTarget.FragmentTarget>()
    private val activityMap = arrayMapOf<Activity, FeatureTarget.ActivityTarget>()
    private lateinit var application: Application
    private lateinit var applicationTarget: FeatureTarget.ApplicationTarget
    private val initializers = LockList.create<Initializer>()

    private val fragmentLock = ReentrantLock()
    private val activityLock = ReentrantLock()
    private val applicationLock = ReentrantLock()

    private fun <R> doOnFragment(action: () -> R): R {
        fragmentLock.lock()
        val result = action()
        fragmentLock.unlock()
        return result
    }


    private fun <R> doOnActivity(action: () -> R): R {
        activityLock.lock()
        val result = action()
        activityLock.unlock()
        return result
    }

    private fun <R> doOnApplication(action: () -> R): R {
        applicationLock.lock()
        val result = action()
        applicationLock.unlock()
        return result
    }

    internal fun addInitializer(initializer: Initializer) {
        initializers.doOnLock {
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


    private fun doOnAction(
        fragment: Fragment,
        action: FragmentFeatureLifecycleObserver.() -> Unit
    ) {
        fragmentLock.lock()
        val target = fragmentMap[fragment]?.featureLifecycle as? FragmentFeatureLifecycleObserver
        if (target != null) {
            action(target)
        }
        fragmentLock.unlock()
    }

    private fun doOnAction(
        activity: Activity,
        action: ActivityFeatureLifecycleObserver.() -> Unit
    ) {
        activityLock.lock()
        val target = activityMap[activity]?.featureLifecycle as? ActivityFeatureLifecycleObserver
        if (target != null) {
            action(target)
        }
        activityLock.unlock()
    }

    private fun doOnAction(
        action: ApplicationFeatureLifecycleObserver.() -> Unit
    ) {
        applicationLock.lock()
        val target = applicationTarget.featureLifecycle as? ApplicationFeatureLifecycleObserver
        if (target != null) {
            action(target)
        }
        applicationLock.unlock()
    }


    private fun setup(fragment: Fragment) {
        doOnFragment {
            if (!fragmentMap.contains(fragment)) {
                fragmentMap[fragment] =
                    FeatureTarget.FragmentTarget(fragment, initializers)
                fragment.childFragmentManager.registerFragmentLifecycleCallbacks(this, false)
            }
        }
    }

    private fun dispose(fragment: Fragment) {
        doOnFragment {
            fragmentMap.remove(fragment)
        }
    }

    private fun setup(activity: Activity) {
        doOnActivity {
            if (!activityMap.contains(activity)) {
                activityMap[activity] =
                    FeatureTarget.ActivityTarget(activity, initializers)
                if (activity is FragmentActivity) {
                    activity.supportFragmentManager.registerFragmentLifecycleCallbacks(this, false)
                }
            }
        }
    }

    private fun dispose(activity: Activity) {
        doOnActivity {
            activityMap.remove(activity)
        }
    }

    internal fun setup(application: Application) {
        applicationLock.lock()
        this.application = application
        applicationTarget = FeatureTarget.ApplicationTarget(application, initializers)
        application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        applicationLock.unlock()
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    private fun isAtLeastQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        setup(activity)
        doOnAction(activity) {
            onActivityPreCreated(activity, savedInstanceState)
        }

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (!isAtLeastQ()) {
            setup(activity)
        }
        doOnAction(activity) {
            onActivityCreated(activity, savedInstanceState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
        doOnAction(activity) {
            onActivityPostCreated(activity, savedInstanceState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreStarted(activity: Activity) {
        doOnAction(activity) {
            onActivityPreStarted(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        doOnAction(activity) {
            onActivityStarted(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostStarted(activity: Activity) {
        doOnAction(activity) {
            onActivityPostStarted(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreResumed(activity: Activity) {
        doOnAction(activity) {
            onActivityPreResumed(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        doOnAction(activity) {
            onActivityResumed(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostResumed(activity: Activity) {
        doOnAction(activity) {
            onActivityPostResumed(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPrePaused(activity: Activity) {
        doOnAction(activity) {
            onActivityPrePaused(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        doOnAction(activity) {
            onActivityPaused(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostPaused(activity: Activity) {
        doOnAction(activity) {
            onActivityPostPaused(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreStopped(activity: Activity) {
        doOnAction(activity) {
            onActivityPreStopped(activity)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        doOnAction(activity) {
            onActivityStopped(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostStopped(activity: Activity) {
        doOnAction(activity) {
            onActivityPostStopped(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
        doOnAction(activity) {
            onActivityPreSaveInstanceState(activity, outState)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        doOnAction(activity) {
            onActivitySaveInstanceState(activity, outState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        doOnAction(activity) {
            onActivityPostSaveInstanceState(activity, outState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreDestroyed(activity: Activity) {
        doOnAction(activity) {
            onActivityPreDestroyed(activity)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        doOnAction(activity) {
            onActivityDestroyed(activity)
        }
        if (!isAtLeastQ()) {
            dispose(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostDestroyed(activity: Activity) {
        doOnAction(activity) {
            onActivityPostDestroyed(activity)
        }
        dispose(activity)
    }

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        setup(f)
        doOnAction(f) {
            onFragmentPreAttached(fm, f, context)
        }
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        doOnAction(f) {
            onFragmentAttached(fm, f, context)
        }
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        doOnAction(f) {
            onFragmentPreCreated(fm, f, savedInstanceState)
        }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        doOnAction(f) {
            onFragmentCreated(fm, f, savedInstanceState)
        }
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        doOnAction(f) {
            onFragmentActivityCreated(fm, f, savedInstanceState)
        }
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        doOnAction(f) {
            onFragmentViewCreated(fm, f, v, savedInstanceState)
        }
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        doOnAction(f) {
            onFragmentStarted(fm, f)
        }
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        doOnAction(f) {
            onFragmentResumed(fm, f)
        }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        doOnAction(f) {
            onFragmentPaused(fm, f)
        }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        doOnAction(f) {
            onFragmentStopped(fm, f)
        }
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        doOnAction(f) {
            onFragmentSaveInstanceState(fm, f, outState)
        }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        doOnAction(f) {
            onFragmentViewDestroyed(fm, f)
        }
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        doOnAction(f) {
            onFragmentDestroyed(fm, f)
        }
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        doOnAction(f) {
            onFragmentDetached(fm, f)
        }
        dispose(f)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> doOnAction {
                onApplicationCreated()
            }

            Lifecycle.Event.ON_START -> doOnAction {
                onApplicationStarted()
            }


            Lifecycle.Event.ON_RESUME -> doOnAction {
                onApplicationResumed()
            }

            Lifecycle.Event.ON_PAUSE ->
                doOnAction {
                    onApplicationPaused()
                }

            Lifecycle.Event.ON_STOP -> doOnAction {
                onApplicationStopped()
            }
            Lifecycle.Event.ON_DESTROY -> {
            }
            Lifecycle.Event.ON_ANY -> {
            }
        }
    }
}