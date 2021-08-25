package com.x930073498.rstore

import com.x930073498.rstore.core.IStoreProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KProperty

interface AnchorScope<T : IStoreProvider> {
    fun onInitialized(action: () -> Unit)


    fun <V> stareAt(
        property: KProperty<V>,
        action: V.() -> Unit
    )
}





