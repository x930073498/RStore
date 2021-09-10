package com.x930073498.rstore

import androidx.lifecycle.MutableLiveData
import com.x930073498.rstore.core.*
import com.x930073498.rstore.internal.propertyValueByDelegate
import com.x930073498.rstore.internal.setEqualsImpl
import com.x930073498.rstore.internal.setFeatureIImpl
import com.x930073498.rstore.internal.setPropertyValueByDelegate
import com.x930073498.rstore.property.FeatureProvider
import com.x930073498.rstore.property.NotifyPropertyDelegate
import com.x930073498.rstore.property.feature.StateAsFeatureProvider
import com.x930073498.rstore.property.equals.ListEquals
import com.x930073498.rstore.property.factory.InstanceFactory
import com.x930073498.rstore.property.factory.MutLiveDataFactory
import com.x930073498.rstore.property.factory.MutableStateFlowFactory
import com.x930073498.rstore.property.factory.TargetPropertyFactory
import com.x930073498.rstore.property.initializer.EmptyInitializer
import com.x930073498.rstore.property.initializer.ObservableLiveDataInitializer
import com.x930073498.rstore.property.initializer.StateFlowInitializer
import com.x930073498.rstore.property.initializer.TargetPropertyInitializer
import com.x930073498.rstore.property.notifier.StandardNotifier
import com.x930073498.rstore.property.transfer.InstanceTransfer
import com.x930073498.rstore.property.transfer.LiveDataTransfer
import com.x930073498.rstore.property.transfer.MutableStateFlowTransfer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1


/**
 * 锚属性 指可以在[com.x930073498.rstore.core.StoreComponent]中通过withAnchor监听的属性
 *
 */

/**
 *
 * 通过默认值初始化代理属性
 * @param defaultValue 属性默认值
 * @param shouldSaveState 是否支持savedState
 * @param isAnchorProperty 是否是锚属性
 * @param equals 属性相等比较器
 */
fun <V> IStoreProviderComponent.property(
    defaultValue: V,
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadWriteProperty<IStoreProviderComponent, V> {
    return NotifyPropertyDelegate(
        this,
        InstanceFactory(defaultValue),
        InstanceTransfer(),
        EmptyInitializer(),
        StandardNotifier(),
        StateAsFeatureProvider(shouldSaveState, isAnchorProperty),
        equals
    )
}

fun <V> IStoreProviderComponent.property(
    defaultValue: V,
    feature: Feature = Feature,
    equals: Equals<V> = DefaultEquals()
): ReadWriteProperty<IStoreProviderComponent, V> {
    return NotifyPropertyDelegate(
        this,
        InstanceFactory(defaultValue),
        InstanceTransfer(),
        EmptyInitializer(),
        StandardNotifier(),
        FeatureProvider(feature),
        equals
    )
}

/**
 * 通过目标属性方法初始化属性，并监听目标属性的变化并自动改变定义属性的值
 * @param property 目标属性（必须是使用类似方式声明的属性，不然无效）
 * @param isAnchorProperty 是否是锚属性
 *  @param equals 属性相等比较器
 * @param transform 属性转化代码块
 */
fun <V, T> IStoreProvider.property(
    property: KProperty0<V>,
    isAnchorProperty: Boolean = false,
    equals: Equals<T> = DefaultEquals(),
    transform: V.() -> T,
): ReadOnlyProperty<IStoreProviderComponent, T> {
    return NotifyPropertyDelegate(
        this,
        TargetPropertyFactory(this, property, transform),
        InstanceTransfer(),
        TargetPropertyInitializer(property,transform),
        StandardNotifier(),
        StateAsFeatureProvider(shouldSaveState = false, isAnchorProperty = isAnchorProperty),
        equals
    )
}

/**
 * 通过目标属性方法初始化属性，并监听目标属性的变化并自动改变定义属性的值
 * @param property 目标属性（必须是使用类似方式声明的属性，不然无效）
 * @param isAnchorProperty 是否是锚属性
 *  @param equals 属性相等比较器
 * @param transform 属性转化代码块
 */
fun <V, T, P : IStoreProvider> P.property(
    property: KProperty1<P, V>,
    isAnchorProperty: Boolean = false,
    equals: Equals<T> = DefaultEquals(),
    transform: V.() -> T,
): ReadOnlyProperty<P, T> {
    return NotifyPropertyDelegate(
        this,
        TargetPropertyFactory(this, property, transform),
        InstanceTransfer(),
        TargetPropertyInitializer(property,transform),
        StandardNotifier(),
        StateAsFeatureProvider(shouldSaveState = false, isAnchorProperty = isAnchorProperty),
        equals
    )
}

/**
 *
 * 通过默认值初始化代理属性
 * @param defaultValue 属性默认值
 * @param isAnchorProperty 是否是锚属性
 * @param equals 属性相等比较器
 */
fun <V> IStoreProvider.property(
    defaultValue: V,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadWriteProperty<IStoreProvider, V> {
    return NotifyPropertyDelegate(
        this,
        InstanceFactory(defaultValue),
        InstanceTransfer(),
        EmptyInitializer(),
        StandardNotifier(),
        StateAsFeatureProvider(false, isAnchorProperty),
        equals
    )
}

/**
 * flow类型的属性，不建议使用
 */
fun <V> IStoreProvider.flowProperty(
    defaultValue: V,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableStateFlow<V>> {
    return NotifyPropertyDelegate(
        this,
        MutableStateFlowFactory(defaultValue),
        MutableStateFlowTransfer(),
        StateFlowInitializer(),
        StandardNotifier(),
        StateAsFeatureProvider(false, isAnchorProperty),
        equals
    )
}

/**
 * liveData类型的属性，不建议使用
 */
fun <V> IStoreProvider.liveDataProperty(
    defaultValue: V? = null,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableLiveData<V>> {
    return NotifyPropertyDelegate(
        this,
        MutLiveDataFactory(defaultValue),
        LiveDataTransfer(),
        ObservableLiveDataInitializer(),
        StandardNotifier(),
        StateAsFeatureProvider(false, isAnchorProperty),
        equals
    )
}

/**
 * flow类型的List属性，不建议使用
 */
fun <V> IStoreProvider.listFlowProperty(
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableStateFlow<List<V>>> {
    return NotifyPropertyDelegate(
        this,
        MutableStateFlowFactory(emptyList()),
        MutableStateFlowTransfer(),
        StateFlowInitializer(),
        StandardNotifier(),
        StateAsFeatureProvider(false, isAnchorProperty),
        ListEquals(equals)
    )
}

/**
 * liveData类型的List属性，不建议使用
 */
fun <V> IStoreProvider.listLiveDataProperty(
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableLiveData<List<V>>> {
    return NotifyPropertyDelegate(
        this,
        MutLiveDataFactory(null),
        LiveDataTransfer(),
        ObservableLiveDataInitializer(),
        StandardNotifier(),
        StateAsFeatureProvider(false, isAnchorProperty),
        ListEquals(equals)
    )
}

/**
 * flow类型属性，不建议使用
 */
fun <V> IStoreProviderComponent.flowProperty(
    defaultValue: V,
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableStateFlow<V>> {
    return NotifyPropertyDelegate(
        this,
        MutableStateFlowFactory(defaultValue),
        MutableStateFlowTransfer(),
        StateFlowInitializer(),
        StandardNotifier(),
        StateAsFeatureProvider(shouldSaveState, isAnchorProperty),
        equals
    )
}

/**
 * livaData类型属性，不建议使用
 */
fun <V> IStoreProviderComponent.liveDataProperty(
    defaultValue: V? = null,
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableLiveData<V>> {
    return NotifyPropertyDelegate(
        this,
        MutLiveDataFactory(defaultValue),
        LiveDataTransfer(),
        ObservableLiveDataInitializer(),
        StandardNotifier(),
        StateAsFeatureProvider(shouldSaveState, isAnchorProperty),
        equals
    )
}

/**
 * flow类型list属性，不建议使用
 */
fun <V> IStoreProviderComponent.listFlowProperty(
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableStateFlow<List<V>>> {
    return NotifyPropertyDelegate(
        this,
        MutableStateFlowFactory(emptyList()),
        MutableStateFlowTransfer(),
        StateFlowInitializer(),
        StandardNotifier(),
        StateAsFeatureProvider(shouldSaveState, isAnchorProperty),
        ListEquals(equals)
    )
}

/**
 * livedata类型list属性，不建议使用
 */
fun <V> IStoreProviderComponent.listLiveDataProperty(
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadOnlyProperty<IStoreProvider, MutableLiveData<List<V>>> {
    return NotifyPropertyDelegate(
        this,
        MutLiveDataFactory(null),
        LiveDataTransfer(),
        ObservableLiveDataInitializer(),
        StandardNotifier(),
        StateAsFeatureProvider(shouldSaveState, isAnchorProperty),
        ListEquals(equals)
    )
}

/**
 *
 * 初始化list类型代理属性
 * @param shouldSaveState 是否支持savedState
 * @param isAnchorProperty 是否是锚属性
 * @param equals 属性相等比较器
 */
fun <V> IStoreProviderComponent.listProperty(
    shouldSaveState: Boolean = false,
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadWriteProperty<IStoreProviderComponent, List<V>> =
    property(emptyList(), shouldSaveState, isAnchorProperty, ListEquals(equals))

/**
 *
 * 初始化list类型代理属性
 * @param isAnchorProperty 是否是锚属性
 * @param equals 属性相等比较器
 */
fun <V> IStoreProvider.listProperty(
    isAnchorProperty: Boolean = false,
    equals: Equals<V> = DefaultEquals()
): ReadWriteProperty<IStoreProvider, List<V>> =
    property(emptyList(), isAnchorProperty, ListEquals(equals))


operator fun <T, V : IStoreProvider> T.getValue(
    thisRef: V,
    property: KProperty<*>
) = propertyValueByDelegate(thisRef, property)


operator fun <T, V : IStoreProvider> T.setValue(
    thisRef: V,
    property: KProperty<*>,
    value: T
) = setPropertyValueByDelegate(thisRef, property, value)


fun IStoreProvider.setFeature(property: KProperty<*>, feature: Feature) =
    setFeatureIImpl(property, feature)

fun<V> IStoreProvider.setEquals(property: KProperty<V>, equals: Equals<V>){
    setEqualsImpl(property, equals)
}