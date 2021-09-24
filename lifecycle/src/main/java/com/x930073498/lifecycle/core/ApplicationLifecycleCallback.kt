package com.x930073498.lifecycle.core

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.x930073498.lifecycle.internal.IStateProvider
import com.x930073498.lifecycle.internal.State

interface ApplicationLifecycleCallback : LifecycleCallback {

    companion object : ApplicationLifecycleCallback {
        internal operator fun ApplicationLifecycleCallback.plus(applicationLifecycleCallback: ApplicationLifecycleCallback): ChainApplicationLifecycleCallback =
            ChainApplicationLifecycleCallback(this, applicationLifecycleCallback)

        internal fun ApplicationLifecycleCallback.wrap(state: State = if (this is IStateProvider) this.state else State()): ApplicationLifecycleCallbackWrapper =
            ApplicationLifecycleCallbackWrapper(this, state)

        internal fun ApplicationLifecycleCallback.asObserver(
            application: Application,
            state: State = if (this is IStateProvider) this.state else State()
        ): ApplicationLifecycleObserver = ApplicationLifecycleObserver(application, this, state)
    }


    fun onApplicationResumed(application: Application) {}

    fun onApplicationStarted(application: Application) {}

    fun onApplicationPaused(application: Application) {}

    fun onApplicationStopped(application: Application) {}

}

internal class ChainApplicationLifecycleCallback(
    private val _first: ApplicationLifecycleCallback,
    private val _second: ApplicationLifecycleCallback,
    override val state: State = State()
) : ApplicationLifecycleCallback, IStateProvider {
    private val first: ApplicationLifecycleCallback
        get() = if (state()) _first else ApplicationLifecycleCallback
    private val second: ApplicationLifecycleCallback
        get() = if (state()) _second else ApplicationLifecycleCallback


    override fun onApplicationResumed(application: Application) {
        first.onApplicationResumed(application)
        second.onApplicationResumed(application)
    }

    override fun onApplicationStarted(application: Application) {
        first.onApplicationStarted(application)
        second.onApplicationStarted(application)
    }

    override fun onApplicationPaused(application: Application) {
        first.onApplicationPaused(application)
        second.onApplicationPaused(application)
    }

    override fun onApplicationStopped(application: Application) {
        first.onApplicationStopped(application)
        second.onApplicationStopped(application)
    }
}

internal class ApplicationLifecycleCallbackWrapper(
    private val _callback: ApplicationLifecycleCallback,
    override val state: State = if (_callback is IStateProvider) _callback.state else State()
) : ApplicationLifecycleCallback, IStateProvider {
    private val callback: ApplicationLifecycleCallback
        get() = if (state()) _callback else ApplicationLifecycleCallback


    override fun onApplicationResumed(application: Application) {
        callback.onApplicationResumed(application)
    }

    override fun onApplicationStarted(application: Application) {
        callback.onApplicationStarted(application)
    }

    override fun onApplicationPaused(application: Application) {
        callback.onApplicationPaused(application)
    }

    override fun onApplicationStopped(application: Application) {
        callback.onApplicationStopped(application)
    }
}

internal class ApplicationLifecycleObserver(
    private val application: Application,
    private val _callback: ApplicationLifecycleCallback,
    override val state: State = if (_callback is IStateProvider) _callback.state else State()
) : LifecycleEventObserver, IStateProvider {
    private val callback: ApplicationLifecycleCallback
        get() = if (state()) _callback else ApplicationLifecycleCallback

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> callback.onApplicationStarted(application)
            Lifecycle.Event.ON_RESUME -> callback.onApplicationResumed(application)
            Lifecycle.Event.ON_PAUSE -> callback.onApplicationPaused(application)
            Lifecycle.Event.ON_STOP -> callback.onApplicationStopped(application)
            else -> {
            }
        }
    }

}