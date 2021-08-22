package com.x930073498.rstore.core


fun <R> ISaveStateStoreProvider.fromSaveStateStore(action: ISaveStateStore.() -> R) =
    action(saveStateStore)

fun ISaveStateStoreProvider.saveState(key: String, value: Any?) =
    fromSaveStateStore { put(key, value) }

fun <T> ISaveStateStoreProvider.getSavedState(key: String) =
    fromSaveStateStore { getInstance<T>(key) }

fun ISaveStateStoreProvider.clear() = fromSaveStateStore { clear() }