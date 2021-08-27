package com.x930073498.rstore.core

interface AnchorScopeLifecycleHandler : Disposable {
    fun launch()

    fun resume()

    fun pause()
}

