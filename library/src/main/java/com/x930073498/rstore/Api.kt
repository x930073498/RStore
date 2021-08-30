package com.x930073498.rstore

import androidx.lifecycle.MutableLiveData
import com.x930073498.rstore.core.DefaultEquals
import com.x930073498.rstore.core.Equals
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.core.IStoreProviderComponent
import com.x930073498.rstore.property.NotifyPropertyDelegate
import com.x930073498.rstore.property.checker.ParamsChecker
import com.x930073498.rstore.property.equals.ListEquals
import com.x930073498.rstore.property.factory.InstanceFactory
import com.x930073498.rstore.property.factory.MutLiveDataFactory
import com.x930073498.rstore.property.factory.MutableStateFlowFactory
import com.x930073498.rstore.property.factory.TargetPropertyFactory
import com.x930073498.rstore.property.initializer.EmptyInitializer
import com.x930073498.rstore.property.notifier.StandardNotifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


fun <V> IStoreProviderComponent.property(
    defaultValue: V,
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadWriteProperty<IStoreProviderComponent, V> {
    return NotifyPropertyDelegate(
        this,
        InstanceFactory(defaultValue),
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState, isAnchorProperty),
        equals
    )
}

fun <V, T> IStoreProvider.property(
    property: KProperty<V>,
    isAnchorProperty: Boolean=false,
    equals: Equals<T> = DefaultEquals(),
    transfer: V.() -> T,
): ReadOnlyProperty<IStoreProviderComponent, T> {
    return NotifyPropertyDelegate(
        this,
        TargetPropertyFactory(property,transfer),
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState = false, isAnchorProperty = isAnchorProperty),
        equals
    )
}

fun <V> IStoreProvider.property(
    defaultValue: V,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadWriteProperty<IStoreProvider, V> {
    return NotifyPropertyDelegate(
        this,
        InstanceFactory(defaultValue),
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(false, isAnchorProperty),
        equals
    )
}

fun <V> IStoreProvider.flowProperty(
    defaultValue: V,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableStateFlow<V>> {
    return NotifyPropertyDelegate(
        this,
        MutableStateFlowFactory(defaultValue),
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(false, isAnchorProperty),
        equals
    )
}

fun <V> IStoreProvider.liveDataProperty(
    defaultValue: V? = null,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableLiveData<V>> {
    return NotifyPropertyDelegate(
        this,
        MutLiveDataFactory(defaultValue),
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(false, isAnchorProperty),
        equals
    )
}

fun <V> IStoreProvider.listFlowProperty(
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableStateFlow<List<V>>> {
    return NotifyPropertyDelegate(
        this,
        MutableStateFlowFactory(emptyList()),
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(false, isAnchorProperty),
        ListEquals(equals)
    )
}

fun <V> IStoreProvider.listLiveDataProperty(
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableLiveData<List<V>>> {
    return NotifyPropertyDelegate(
        this,
        MutLiveDataFactory(null),
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(false, isAnchorProperty),
        ListEquals(equals)
    )
}

fun <V> IStoreProviderComponent.flowProperty(
    defaultValue: V,
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableStateFlow<V>> {
    return NotifyPropertyDelegate(
        this,
        MutableStateFlowFactory(defaultValue),
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState, isAnchorProperty),
        equals
    )
}


fun <V> IStoreProviderComponent.liveDataProperty(
    defaultValue: V? = null,
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableLiveData<V>> {
    return NotifyPropertyDelegate(
        this,
        MutLiveDataFactory(defaultValue),
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState, isAnchorProperty),
        equals
    )
}

fun <V> IStoreProviderComponent.listFlowProperty(
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableStateFlow<List<V>>> {
    return NotifyPropertyDelegate(
        this,
        MutableStateFlowFactory(emptyList()),
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState, isAnchorProperty),
        ListEquals(equals)
    )
}

fun <V> IStoreProviderComponent.listLiveDataProperty(
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableLiveData<List<V>>> {
    return NotifyPropertyDelegate(
        this,
        MutLiveDataFactory(null),
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState, isAnchorProperty),
        ListEquals(equals)
    )
}

fun <V> IStoreProviderComponent.listProperty(
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadWriteProperty<IStoreProviderComponent, List<V>> =
    property(emptyList(), shouldSaveState, isAnchorProperty, ListEquals(equals))

fun <V> IStoreProvider.listProperty(
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadWriteProperty<IStoreProvider, List<V>> =
    property(emptyList(), isAnchorProperty, ListEquals(equals))

