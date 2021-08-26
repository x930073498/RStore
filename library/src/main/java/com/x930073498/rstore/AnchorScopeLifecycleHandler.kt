package com.x930073498.rstore

interface AnchorScopeLifecycleHandler : Disposable {
    fun launch()

    fun resume()

    fun pause()
}

