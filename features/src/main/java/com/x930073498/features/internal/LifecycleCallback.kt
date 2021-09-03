package com.x930073498.features.internal

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import java.lang.IllegalArgumentException

internal object LifecycleCallback : Application.ActivityLifecycleCallbacks,
    FragmentManager.FragmentLifecycleCallbacks() {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
    private fun isAtLeastQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    fun register(application: Application) {
        application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(FeatureAgent)

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        FeatureAgent.setup(activity, this)
        FeatureAgent.doOnLifecycle(activity) {
            onActivityCreated(activity, savedInstanceState)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityStarted(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityResumed(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPaused(activity)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityStarted(activity)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivitySaveInstanceState(activity, outState)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityDestroyed(activity)
        }
        if (!isAtLeastQ())
            FeatureAgent.dispose(activity, this)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        FeatureAgent.setup(activity, this)
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPreCreated(activity, savedInstanceState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPostCreated(activity, savedInstanceState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreStarted(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPreStarted(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostStarted(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPostStarted(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreResumed(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPreResumed(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostResumed(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPostResumed(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPrePaused(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPrePaused(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostPaused(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPostPaused(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreStopped(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPreStopped(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostStopped(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPostStopped(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPreSaveInstanceState(activity, outState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPostSaveInstanceState(activity, outState)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPreDestroyed(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPreDestroyed(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityPostDestroyed(activity: Activity) {
        FeatureAgent.doOnLifecycle(activity) {
            onActivityPostDestroyed(activity)
        }
        FeatureAgent.dispose(activity, this)
    }

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        FeatureAgent.setup(f)
        FeatureAgent.doOnLifecycle(f) {
            onFragmentPreAttached(fm, f, context)
        }
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        FeatureAgent.doOnLifecycle(f) {
            onFragmentAttached(fm, f, context)
        }
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        FeatureAgent.doOnLifecycle(f) {
            onFragmentPreCreated(fm, f, savedInstanceState)
        }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        FeatureAgent.doOnLifecycle(f) {
            onFragmentCreated(fm, f, savedInstanceState)
        }
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        FeatureAgent.doOnLifecycle(f) {
            onFragmentActivityCreated(fm, f, savedInstanceState)
        }
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        FeatureAgent.doOnLifecycle(f) {
            onFragmentViewCreated(fm, f, v, savedInstanceState)
        }
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        FeatureAgent.doOnLifecycle(f) {
            onFragmentStarted(fm, f)
        }
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        FeatureAgent.doOnLifecycle(f) {
            onFragmentResumed(fm, f)
        }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        FeatureAgent.doOnLifecycle(f) {
            onFragmentPaused(fm, f)
        }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        FeatureAgent.doOnLifecycle(f) {
            onFragmentStopped(fm, f)
        }
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        FeatureAgent.doOnLifecycle(f) {
            onFragmentSaveInstanceState(fm, f, outState)
        }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        FeatureAgent.doOnLifecycle(f) {
            onFragmentViewDestroyed(fm, f)
        }
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        FeatureAgent.doOnLifecycle(f) {
            onFragmentDestroyed(fm, f)
        }
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        FeatureAgent.doOnLifecycle(f) {
            onFragmentDetached(fm, f)
        }
        FeatureAgent.dispose(f)
    }


}