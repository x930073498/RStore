package com.x930073498.rstore

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


class LifecycleAnchorStarter(
    private val lifecycleOwner: LifecycleOwner,
    private val option: Option = Option.ON_RESUME
) : AnchorStarter {
    enum class Option {
        ON_CREATE, ON_RESUME
    }

    override fun start(handler: AnchorScopeLifecycleHandler) {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                handler.launch()
                if (option == Option.ON_CREATE) {
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