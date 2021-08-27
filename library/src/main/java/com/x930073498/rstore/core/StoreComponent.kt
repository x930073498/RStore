@file:Suppress("FunctionName")

package com.x930073498.rstore.core

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import com.x930073498.rstore.LifecycleAnchorStarter
import com.x930073498.rstore.internal.awaitUntilImpl
import com.x930073498.rstore.internal.registerAnchorPropertyChangedListenerImpl
import com.x930073498.rstore.internal.registerPropertyChangedListenerImpl
import com.x930073498.rstore.internal.setSavedStateRegistryOwner

import kotlin.reflect.KProperty

interface StoreComponent : SavedStateRegistryOwner {
    suspend fun <T : IStoreProvider, V> T.awaitUntil(
        property: KProperty<V>,
        predicate: suspend V.() -> Boolean
    ) = with(this@StoreComponent) {
        setSavedStateRegistryOwner(this)
        awaitUntilImpl(property, predicate)
    }

    fun <T : IStoreProvider, V> T.withProperty(
        property: KProperty<V>,
        lifecycleOwner: LifecycleOwner = defaultLifecycleOwner,
        action: V.() -> Unit
    ) = with(this@StoreComponent) {
        setSavedStateRegistryOwner(this)
        registerPropertyChangedListenerImpl(property, lifecycleOwner, action)
    }


    fun <T : IStoreProvider> T.withAnchor(
        starter: AnchorStarter = LifecycleAnchorStarter(
            defaultLifecycleOwner,
            false
        ),
        action: AnchorScope<T>.(T) -> Unit
    ) = with(this@StoreComponent) {
        setSavedStateRegistryOwner(this)
        registerAnchorPropertyChangedListenerImpl(starter, action)
    }

}

val StoreComponent.defaultLifecycleOwner: LifecycleOwner
    get() {
        return when (this) {
            is Fragment -> {
                viewLifecycleOwnerLiveData.value ?: this
            }
            else -> this
        }
    }


fun StoreComponent.LifecycleAnchorStarter(onCreateResume: () -> Boolean): AnchorStarter {
    return LifecycleAnchorStarter(defaultLifecycleOwner, onCreateResume)
}

fun StoreComponent.LifecycleAnchorStarter(onCreateResume: Boolean = false): AnchorStarter {
    return LifecycleAnchorStarter(defaultLifecycleOwner, onCreateResume)
}