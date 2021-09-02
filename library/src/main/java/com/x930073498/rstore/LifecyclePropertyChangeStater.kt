package com.x930073498.rstore

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.x930073498.rstore.core.PropertyChangeStater
import com.x930073498.rstore.util.AwaitState

class LifecyclePropertyChangeStater constructor(
    private val lifecycleOwner: LifecycleOwner,
    private val onCreateResume: () -> Boolean = { false }
) :
    PropertyChangeStater {
    constructor(lifecycleOwner: LifecycleOwner, onCreateResume: Boolean) : this(lifecycleOwner,
        { onCreateResume })
    override fun start(startHandle: AwaitState<Boolean>) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) return
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                if (onCreateResume()) {
                    startHandle.setState(true)
                }
            }

            override fun onResume(owner: LifecycleOwner) {
                startHandle.setState(true)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                lifecycleOwner.lifecycle.removeObserver(this)
            }
        })
    }
}