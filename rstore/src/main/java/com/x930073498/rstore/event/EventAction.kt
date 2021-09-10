package com.x930073498.rstore.event

import com.x930073498.rstore.core.IStoreProvider

interface EventAction<S : IStoreProvider, T, R> {
    suspend fun S.process(data: T): R
}

