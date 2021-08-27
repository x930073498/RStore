package com.x930073498.rstore.anchor

import com.x930073498.rstore.core.AnchorScopeState
import com.x930073498.rstore.core.AnchorStateEventAction
import com.x930073498.rstore.core.IStoreProvider

internal class InitializationAction<T : IStoreProvider>(
    private val action: suspend () -> Unit = {}
) : AnchorStateEventAction<T> {

    override fun T.enable(data: AnchorScopeState): Boolean {
        return !data.isInitialized
    }

    override suspend fun T.process(data: AnchorScopeState) {
        action()
    }


}