package com.x930073498.rstore.core

import com.x930073498.rstore.event.EventAction

data class AnchorScopeState(
    var isInitialized: Boolean,
    val container: PropertyContainer
)
typealias  AnchorStateEventAction<T> = EventAction<T, AnchorScopeState>