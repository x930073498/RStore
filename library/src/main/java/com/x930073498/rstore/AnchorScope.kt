package com.x930073498.rstore

import com.x930073498.rstore.core.IStoreProvider
import kotlin.reflect.KProperty

interface AnchorScope<T : IStoreProvider> {
    fun onInitialized(action: () -> Unit)


    fun <V> stareAt(
        property: KProperty<V>,
        action: V.() -> Unit
    )
}


