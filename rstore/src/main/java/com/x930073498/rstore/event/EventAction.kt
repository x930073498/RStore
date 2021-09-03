package com.x930073498.rstore.event

import com.x930073498.rstore.core.IStoreProvider

fun interface EventAction<S:IStoreProvider,T> {
    suspend fun S.process(data: T)
}

