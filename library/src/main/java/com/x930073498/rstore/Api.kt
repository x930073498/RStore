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
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState, isAnchorProperty),
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
        TargetPropertyFactory(property, transform),
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState = false, isAnchorProperty = isAnchorProperty),
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
        TargetPropertyFactory(property, transform),
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState = false, isAnchorProperty = isAnchorProperty),
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
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(false, isAnchorProperty),
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
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(false, isAnchorProperty),
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
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(false, isAnchorProperty),
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
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(false, isAnchorProperty),
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
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(false, isAnchorProperty),
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
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState, isAnchorProperty),
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
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState, isAnchorProperty),
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
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState, isAnchorProperty),
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
        EmptyInitializer(),
        StandardNotifier(),
        ParamsChecker(shouldSaveState, isAnchorProperty),
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

