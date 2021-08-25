package com.x930073498.rstore


interface AnchorStarter {
    companion object : AnchorStarter {
        override fun start(handler: AnchorScopeLifecycleHandler) {
            handler.launch()
            handler.resume()
        }
    }
    fun start(handler: AnchorScopeLifecycleHandler)

}