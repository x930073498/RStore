package com.x930073498.rstore.core

fun ISaveStateStore.saveState(key: String, value: Any?) = put(key, value)
fun <R> ISaveStateStore.getInstance(key: String): R? {
    return get(key) as? R
}
fun <T> ISaveStateStore.getSavedState(key: String) = getInstance<T>(key)