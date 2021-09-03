package com.x930073498.rstore.core

import com.x930073498.rstore.anchor.AnchorPropertyStateHolder
import com.x930073498.rstore.event.EventAction
import com.x930073498.rstore.util.AwaitState

data class AnchorScopeState(
    var isInitialized: Boolean,
    val stateHolder: AnchorPropertyStateHolder,
    val resumeAwaitState:AwaitState<Boolean>,
):Disposable{
    override fun dispose() {
        stateHolder.dispose()
    }
}
typealias  AnchorStateEventAction<T> = EventAction<T, AnchorScopeState>