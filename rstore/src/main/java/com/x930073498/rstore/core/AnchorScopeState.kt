package com.x930073498.rstore.core

import com.x930073498.rstore.event.EventAction
import com.x930073498.rstore.util.AwaitState

data class AnchorScopeState(
    var isInitialized: Boolean,
    val stateHolder: AnchorPropertyStateHolder,
    val valueHolder: AnchorPropertyValueHolder,
    val resumeAwaitState:AwaitState<Boolean>,
):Disposable{
    override fun dispose() {
        stateHolder.dispose()
    }
}
typealias  AnchorStateEventAction<T,R> = EventAction<T, AnchorScopeState,R>