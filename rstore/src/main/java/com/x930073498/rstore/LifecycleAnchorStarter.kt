package com.x930073498.rstore

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.x930073498.rstore.core.AnchorScopeLifecycleHandler
import com.x930073498.rstore.core.AnchorStarter


class LifecycleAnchorStarter constructor(
    private val lifecycleOwner: LifecycleOwner,
    private val onCreateResume: () -> Boolean = { false }
) : AnchorStarter {

     constructor(lifecycleOwner: LifecycleOwner, onCreateResume: Boolean) : this(lifecycleOwner,
        { onCreateResume })

    override fun start(handler: AnchorScopeLifecycleHandler) {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                handler.launch()
                if (onCreateResume()) {
                    handler.resume()
                }
            }

            override fun onResume(owner: LifecycleOwner) {
                handler.resume()
            }

            override fun onPause(owner: LifecycleOwner) {
                handler.pause()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                handler.dispose()
            }
        })
    }
}