package com.x930073498.rstore

import com.x930073498.rstore.core.IStoreProvider
import kotlin.reflect.KProperty

internal fun <T : IStoreProvider> AnchorScope<T>.stareAt(vararg property: KProperty<*>,equals: Equals<Any?> = DefaultEquals(), action: () -> Unit) {
    property.forEach { stareAt(it,equals) { action() } }
}