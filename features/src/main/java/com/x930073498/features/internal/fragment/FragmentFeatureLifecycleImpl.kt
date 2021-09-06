package com.x930073498.features.internal.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Initializer
import com.x930073498.features.core.fragment.FragmentFeatureLifecycle
import com.x930073498.features.core.fragment.FragmentFeatureLifecycleObserver
import com.x930073498.features.internal.LockList
import com.x930073498.features.internal.doOnLock
import com.x930073498.features.internal.forEach

class FragmentFeatureLifecycleImpl(
    private val target: FeatureTarget.FragmentTarget,
    private val initializers: LockList<Initializer>
) :
    FragmentFeatureLifecycle,
    FragmentFeatureLifecycleObserver,
    LockList<FragmentFeatureLifecycleObserver> by LockList.create() {
    init {
        initializers.forEach {
            init(target)
        }
    }


    override fun addObserver(observer: FragmentFeatureLifecycleObserver) {
        doOnLock {
            if (!contains(observer)) add(observer)
        }

    }

    override fun removeObserver(observer: FragmentFeatureLifecycleObserver) {
        doOnLock {
            remove(observer)
        }
    }

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {

        forEach {
            onFragmentPreAttached(fm, f, context)
        }
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        forEach { onFragmentAttached(fm, f, context) }
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        forEach {
            onFragmentPreCreated(fm, f, savedInstanceState)
        }

    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        forEach {
            onFragmentCreated(fm, f, savedInstanceState)
        }
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        forEach {
            onFragmentActivityCreated(fm, f, savedInstanceState)
        }
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        forEach {
            onFragmentViewCreated(fm, f, v, savedInstanceState)
        }
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        forEach {
            onFragmentStarted(fm, f)
        }
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        forEach {
            onFragmentResumed(fm, f)
        }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        forEach {
            onFragmentPaused(fm, f)
        }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        forEach {
            onFragmentStopped(fm, f)
        }
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        forEach {
            onFragmentSaveInstanceState(fm, f, outState)
        }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        forEach {
            onFragmentViewDestroyed(fm, f)
        }
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        forEach {
            onFragmentDestroyed(fm, f)
        }
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        forEach {
            onFragmentDetached(fm, f)
        }
    }
}