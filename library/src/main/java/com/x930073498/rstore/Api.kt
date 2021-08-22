package com.x930073498.rstore

import androidx.lifecycle.LifecycleOwner
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.NotifyPropertyDelegate
import com.x930073498.rstore.property.checker.ParamsChecker
import com.x930073498.rstore.property.factory.InstanceFactory
import com.x930073498.rstore.property.initializer.InstanceInitializer
import com.x930073498.rstore.property.notifier.StandardNotifier
import com.x930073498.rstore.property.registerAnchorPropertyChangedListenerImpl
import com.x930073498.rstore.property.registerPropertyChangedListenerImpl
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


fun <T : IStoreProvider, V> T.registerPropertyChangedListener(
    property: KProperty<V>,
    lifecycleOwner: LifecycleOwner? = null,
    action: V.() -> Unit
) = registerPropertyChangedListenerImpl(property, lifecycleOwner, action)

fun <T : IStoreProvider> T.registerAnchorPropertyChangedListener(
    storeComponent: StoreComponent,
    lifecycleOwner: LifecycleOwner = storeComponent,
    action: T.(AnchorScope<T>) -> Unit
) = registerAnchorPropertyChangedListenerImpl(storeComponent, lifecycleOwner, action)


fun <T : IStoreProvider, V> property(
    defaultValue: V,
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadWriteProperty<T, V> {
    return NotifyPropertyDelegate(
        InstanceFactory(defaultValue),
        InstanceInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState, isAnchorProperty),
        equals
    )
}

fun <T : IStoreProvider, V> listProperty(
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadWriteProperty<T, List<V>> =
    property(emptyList(), shouldSaveState, isAnchorProperty, ListEquals(equals))


