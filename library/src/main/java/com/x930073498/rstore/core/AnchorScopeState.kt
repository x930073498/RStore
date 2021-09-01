package com.x930073498.rstore.core

import com.x930073498.rstore.event.EventAction
import com.x930073498.rstore.util.AwaitState

data class AnchorScopeState(
    var isInitialized: Boolean,
    val resumeAwaitState:AwaitState<Boolean>,
)
typealias  AnchorStateEventAction<T> = EventAction<T, AnchorScopeState>