package com.x930073498.rstore

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.NotifyPropertyDelegate
import com.x930073498.rstore.property.checker.ParamsChecker
import com.x930073498.rstore.property.equals.ListEquals
import com.x930073498.rstore.property.factory.InstanceFactory
import com.x930073498.rstore.property.factory.MutLiveDataFactory
import com.x930073498.rstore.property.factory.MutableStateFlowFactory
import com.x930073498.rstore.property.initializer.EmptyInitializer
import com.x930073498.rstore.property.notifier.StandardNotifier
import com.x930073498.rstore.property.registerAnchorPropertyChangedListenerImpl
import com.x930073498.rstore.property.registerPropertyChangedListenerImpl
import com.x930073498.rstore.property.stareAtImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadOnlyProperty
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
    option: LifecycleAnchorStarter.Option = LifecycleAnchorStarter.Option.ON_RESUME,
    action: T.(AnchorScope<T>) -> Unit
) = registerAnchorPropertyChangedListener(
    storeComponent,
    LifecycleAnchorStarter(lifecycleOwner, option),
    action
)

fun <T : IStoreProvider> T.registerAnchorPropertyChangedListener(
    storeComponent: StoreComponent,
    starter: AnchorStarter = AnchorStarter,
    action: T.(AnchorScope<T>) -> Unit
) = registerAnchorPropertyChangedListenerImpl(
    storeComponent,
    starter,
    action
)


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


fun <T : IStoreProvider> StoreComponent.withAnchor(
    provider: T,
    lifecycleOwner: LifecycleOwner = defaultLifecycleOwner,
    option: LifecycleAnchorStarter.Option = LifecycleAnchorStarter.Option.ON_RESUME,
    action: T.(AnchorScope<T>) -> Unit
) {
    provider.registerAnchorPropertyChangedListener(this, lifecycleOwner, option, action)
}

fun <T : IStoreProvider> StoreComponent.withAnchor(
    provider: T,
    starter: AnchorStarter = LifecycleAnchorStarter(
        defaultLifecycleOwner,
        LifecycleAnchorStarter.Option.ON_RESUME
    ),
    action: T.(AnchorScope<T>) -> Unit
) {
    provider.registerAnchorPropertyChangedListener(this, starter, action)
}

fun <T : IStoreProvider, V> StoreComponent.withProperty(
    provider: T,
    property: KProperty<V>,
    lifecycleOwner: LifecycleOwner = defaultLifecycleOwner,
    action: V.() -> Unit
) {
    provider.registerPropertyChangedListener(property, lifecycleOwner, action)
}

fun <T : IStoreProvider> AnchorScope<T>.stareAt(
    vararg property: KProperty<*>,
    action: () -> Unit
) = stareAtImpl(property = property, action)
