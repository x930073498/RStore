package com.x930073498.rstore.core

fun ISaveStateStore.saveState(key: String, value: Any?) = put(key, value)

fun <T> ISaveStateStore.getSavedState(key: String) = getInstance<T>(key)