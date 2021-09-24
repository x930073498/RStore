package com.x930073498.lifecycle.core

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.x930073498.lifecycle.internal.IStateProvider
import com.x930073498.lifecycle.internal.State

interface FragmentLifecycleCallback : LifecycleCallback {
    companion object : FragmentLifecycleCallback {
        internal operator fun FragmentLifecycleCallback.plus(callback: FragmentLifecycleCallback): ChainFragmentLifecycleCallbackWrapper {
            return ChainFragmentLifecycleCallbackWrapper(this, callback)
        }

        internal fun FragmentLifecycleCallback.wrap(state: State = if (this is IStateProvider) this.state else State()): FragmentLifecycleCallbackWrapper {
            return FragmentLifecycleCallbackWrapper(this, state)
        }

    }

    fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
    }

    fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
    }

    fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
    }

    fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
    }

    fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
    }

    fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
    }

    fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
    }

    fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
    }
}

internal class ChainFragmentLifecycleCallbackWrapper(
    private val _first: FragmentLifecycleCallback,
    private val _second: FragmentLifecycleCallback,
    override val state: State = State()
) : IStateProvider, FragmentLifecycleCallback {
    private val first: FragmentLifecycleCallback
        get() = if (state()) _first else FragmentLifecycleCallback
    private val second: FragmentLifecycleCallback
        get() = if (state()) _second else FragmentLifecycleCallback

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        first.onFragmentPreAttached(fm, f, context)
        second.onFragmentPreAttached(fm, f, context)
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        first.onFragmentAttached(fm, f, context)
        second.onFragmentAttached(fm, f, context)
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        first.onFragmentPreCreated(fm, f, savedInstanceState)
        second.onFragmentPreCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        first.onFragmentCreated(fm, f, savedInstanceState)
        second.onFragmentCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        first.onFragmentActivityCreated(fm, f, savedInstanceState)
        second.onFragmentActivityCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        first.onFragmentViewCreated(fm, f, v, savedInstanceState)
        second.onFragmentViewCreated(fm, f, v, savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        first.onFragmentStarted(fm, f)
        second.onFragmentStarted(fm, f)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        first.onFragmentResumed(fm, f)
        second.onFragmentResumed(fm, f)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        first.onFragmentPaused(fm, f)
        second.onFragmentPaused(fm, f)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        first.onFragmentStopped(fm, f)
        second.onFragmentStopped(fm, f)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        first.onFragmentSaveInstanceState(fm, f, outState)
        second.onFragmentSaveInstanceState(fm, f, outState)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        first.onFragmentViewDestroyed(fm, f)
        second.onFragmentViewDestroyed(fm, f)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        first.onFragmentDestroyed(fm, f)
        second.onFragmentDestroyed(fm, f)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        first.onFragmentDetached(fm, f)
        second.onFragmentDetached(fm, f)
    }
}

internal class FragmentLifecycleCallbackWrapper(
    private val _callback: FragmentLifecycleCallback,
    override val state: State = if (_callback is IStateProvider) _callback.state else State()
) : IStateProvider, FragmentLifecycleCallback {
    private val callback: FragmentLifecycleCallback
        get() = if (state()) _callback else FragmentLifecycleCallback


    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        callback.onFragmentPreAttached(fm, f, context)
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        callback.onFragmentAttached(fm, f, context)
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        callback.onFragmentPreCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        callback.onFragmentCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        callback.onFragmentActivityCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        callback.onFragmentViewCreated(fm, f, v, savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        callback.onFragmentStarted(fm, f)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        callback.onFragmentResumed(fm, f)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        callback.onFragmentPaused(fm, f)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        callback.onFragmentStopped(fm, f)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        callback.onFragmentSaveInstanceState(fm, f, outState)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        callback.onFragmentViewDestroyed(fm, f)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        callback.onFragmentDestroyed(fm, f)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        callback.onFragmentDetached(fm, f)
    }
}


internal class ChainFragmentLifecycleCallback(
    private val _first: FragmentLifecycleCallback, private val _second: FragmentLifecycleCallback,
    override val state: State = State()
) : FragmentLifecycleCallback, IStateProvider {
    private val first: FragmentLifecycleCallback
        get() = if (state()) _first else FragmentLifecycleCallback

    private val second: FragmentLifecycleCallback
        get() = if (state()) _second else FragmentLifecycleCallback

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        first.onFragmentPreAttached(fm, f, context)
        second.onFragmentPreAttached(fm, f, context)
    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        first.onFragmentAttached(fm, f, context)
        second.onFragmentAttached(fm, f, context)
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        first.onFragmentPreCreated(fm, f, savedInstanceState)
        second.onFragmentPreCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        first.onFragmentCreated(fm, f, savedInstanceState)
        second.onFragmentCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        first.onFragmentActivityCreated(fm, f, savedInstanceState)
        second.onFragmentActivityCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        first.onFragmentViewCreated(fm, f, v, savedInstanceState)
        second.onFragmentViewCreated(fm, f, v, savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        first.onFragmentStarted(fm, f)
        second.onFragmentStarted(fm, f)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        first.onFragmentResumed(fm, f)
        second.onFragmentResumed(fm, f)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        first.onFragmentPaused(fm, f)
        second.onFragmentPaused(fm, f)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        first.onFragmentStopped(fm, f)
        second.onFragmentStopped(fm, f)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        first.onFragmentSaveInstanceState(fm, f, outState)
        second.onFragmentSaveInstanceState(fm, f, outState)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        first.onFragmentViewDestroyed(fm, f)
        second.onFragmentViewDestroyed(fm, f)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        first.onFragmentDestroyed(fm, f)
        second.onFragmentDestroyed(fm, f)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        first.onFragmentDetached(fm, f)
        second.onFragmentDetached(fm, f)
    }
}

internal class FragmentManagerLifecycleCallback(
    private val _callback: FragmentLifecycleCallback,
    override val state: State = if (_callback is IStateProvider) _callback.state else State()
) :
    FragmentManager.FragmentLifecycleCallbacks(), IStateProvider {

    private val callback: FragmentLifecycleCallback
        get() = if (state()) _callback else FragmentLifecycleCallback

    override fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
        callback.onFragmentPreAttached(fm, f, context)

    }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        callback.onFragmentAttached(fm, f, context)
    }

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        callback.onFragmentPreCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        callback.onFragmentCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
        callback.onFragmentActivityCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
        callback.onFragmentViewCreated(fm, f, v, savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        callback.onFragmentStarted(fm, f)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        callback.onFragmentResumed(fm, f)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        callback.onFragmentPaused(fm, f)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        callback.onFragmentStopped(fm, f)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        callback.onFragmentSaveInstanceState(fm, f, outState)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        callback.onFragmentViewDestroyed(fm, f)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        callback.onFragmentDestroyed(fm, f)
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        callback.onFragmentDetached(fm, f)
    }
}