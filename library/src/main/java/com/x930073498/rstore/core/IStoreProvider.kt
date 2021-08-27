package com.x930073498.rstore.core

import androidx.savedstate.SavedStateRegistryOwner

fun <R> IStoreProvider.fromStore(action: IStore.() -> R): R {
    return action(store)
}



fun IStoreProvider.clearStore() {
    fromStore {
        clear()
    }
}