package com.x930073498.rstore.core

import com.x930073498.rstore.anchor.InitializationAction
import com.x930073498.rstore.anchor.PropertyAction
import kotlin.reflect.KProperty

abstract class AnchorScope<T : IStoreProvider> {
    internal abstract fun pushAction(eventAction: AnchorStateEventAction<T,*>)
}

fun <T : IStoreProvider, V> AnchorScope<T>.stareAt(
    property: KProperty<V>,
    action: V.() -> Unit
) {
    pushAction(PropertyAction(property, action))
}

fun <T : IStoreProvider> AnchorScope<T>.onInitialized(action: () -> Unit) {
    pushAction(InitializationAction { action() })
}






