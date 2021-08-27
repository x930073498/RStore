package com.x930073498.rstore.event

import com.x930073498.rstore.core.IStoreProvider

interface EventAction<S:IStoreProvider,T> {
    fun S.init(data: T){}
    fun S.enable(data: T): Boolean
    suspend fun S.process(data: T)
}

