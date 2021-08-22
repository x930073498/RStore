package com.x930073498.rstore

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.x930073498.rstore.core.IStoreProvider
import kotlin.reflect.KProperty

interface StoreComponent : LifecycleOwner {

    fun <T : IStoreProvider, V> T.withStateChanged(
        property: KProperty<V>,
        lifecycleOwner: LifecycleOwner = defaultLifecycleOwner,
        action: V.() -> Unit
    ) {
        registerPropertyChangedListener(property, lifecycleOwner, action)
    }

    fun <T : IStoreProvider> T.withAnchorStateChanged(
        lifecycleOwner: LifecycleOwner = defaultLifecycleOwner,
        action: T.(AnchorScope<T>) -> Unit
    ) {
        registerAnchorPropertyChangedListener(this@StoreComponent, lifecycleOwner, action)
    }
}

internal val StoreComponent.defaultLifecycleOwner: LifecycleOwner
    get() {
        return when (this) {
            is Activity -> this
            is Fragment -> {
                viewLifecycleOwnerLiveData.value ?: this
            }
            else -> this
        }
    }