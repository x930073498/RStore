@file:Suppress("FunctionName")

package com.x930073498.rstore

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.x930073498.rstore.core.IStoreProvider
import kotlin.reflect.KProperty

interface StoreComponent : LifecycleOwner {

    fun <T : IStoreProvider, V> T.withProperty(
        property: KProperty<V>,
        lifecycleOwner: LifecycleOwner = defaultLifecycleOwner,
        action: V.() -> Unit
    ) {
        registerPropertyChangedListener(property, lifecycleOwner, action)
    }

    fun <T : IStoreProvider> T.withAnchor(
        starter: AnchorStarter = LifecycleAnchorStarter(
            defaultLifecycleOwner,
            false
        ),
        action: T.(AnchorScope<T>) -> Unit
    ) {
        registerAnchorPropertyChangedListener(starter, action)
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