package com.x930073498.rstore.anchor

import com.x930073498.rstore.core.AnchorScopeState
import com.x930073498.rstore.core.AnchorStateEventAction
import com.x930073498.rstore.core.IStoreProvider
import kotlinx.coroutines.withContext

internal class InitializationAction<T : IStoreProvider>(
    private val action: suspend () -> Unit = {}
) : AnchorStateEventAction<T,Unit> {

    override suspend fun T.process(data: AnchorScopeState) {
        if (data.isInitialized) return
        withContext(main) {
            action()
        }
    }


}