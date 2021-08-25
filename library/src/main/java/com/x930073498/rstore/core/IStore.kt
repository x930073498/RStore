package com.x930073498.rstore.core

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

fun <R> IStore.getInstance(key: Any): R? {
    return get(key) as? R
}

@OptIn(InternalCoroutinesApi::class)
fun <R> IStore.getOrCreate(key: Any, action: () -> R): R = synchronized(this) {
    val result = getInstance<R>(key)
    if (result != null) return result
    return action().apply {
        put(key,this)
    }
}
