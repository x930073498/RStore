@file:Suppress("LocalVariableName")

package com.x930073498.rstore.internal

import androidx.lifecycle.*
import com.x930073498.rstore.*
import com.x930073498.rstore.anchor.AnchorScopeImpl
import com.x930073498.rstore.core.*
import com.x930073498.rstore.core.Feature.Companion.hasFeature
import com.x930073498.rstore.core.Feature.Companion.minus
import com.x930073498.rstore.core.Feature.Companion.plus
import com.x930073498.rstore.core.Feature.Companion.replace
import com.x930073498.rstore.property.FeatureProvider
import com.x930073498.rstore.property.NotifyPropertyDelegate
import com.x930073498.rstore.property.factory.InstanceFactory
import com.x930073498.rstore.property.initializer.EmptyInitializer
import com.x930073498.rstore.property.notifier.StandardNotifier
import com.x930073498.rstore.property.transfer.InstanceTransfer
import com.x930073498.rstore.util.AwaitState
import com.x930073498.rstore.util.HeartBeat
import com.x930073498.rstore.util.asFlow
import com.x930073498.rstore.util.awaitState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1


private const val anchorPropertyEventChannelKey = "1306d9dd-5824-4341-af54-d45265fc2a1e"
private const val anchorPropertyEventsFlowKey = "4dd1aea6-5f82-43be-bddd-d538b5961a38"
private const val propertyFeatureKey = "842c085b-2b40-43db-950d-ba2274c80c53"
private const val propertyEqualsKey = "403bb274-2e31-4a19-904f-cd39234739be"


internal data class PropertyEvent(
    val property: KProperty<*>,
//    val equals: Equals<*>,
    val id: String = UUID.randomUUID().toString()
)

private fun KProperty<*>.asEvent() =
    PropertyEvent(this)

internal fun <T : IStoreProvider, V> T.valueFromProperty(kProperty: KProperty<V>): V? {
    return with(kProperty) {
        when (this) {
            is KProperty0 -> invoke()
            is KProperty1<*, V> -> {
                runCatching {
                    this as KProperty1<T, V>
                    invoke(this@valueFromProperty)
                }.getOrNull()
            }
            else -> null
        }
    }
}


internal fun <T : IStoreProvider, V> T.invokeAction(
    kProperty: KProperty<V>,
    action: V.() -> Unit
) {
    valueFromProperty(kProperty)?.apply {
        action()
    }
}

internal fun IStoreProvider.notifyPropertyChanged(property: KProperty<*>) {
    val heartBeat = fromStore {
        getInstance<HeartBeat>(propertyHeartBeatKey(property))
    } ?: return
    heartBeat.beat()
}


internal suspend fun <T : IStoreProvider, V> T.awaitUntilImpl(
    property: KProperty<V>,
    predicate: suspend V.() -> Boolean
): V {
    val heartBeat = fromStore {
        getOrCreate(propertyHeartBeatKey(property)) {
            HeartBeat.create()
        }
    }
    return heartBeat.asFlow().map { valueFromProperty(property) as V }.first {
        predicate(it)
    }
}

internal fun <T : IStoreProvider, V> T.registerPropertyChangedListenerImpl(
    property: KProperty<V>,
    stater: PropertyChangeStater = PropertyChangeStater,
    action: V.() -> Unit
): Disposable {
    val heartBeat = fromStore {
        getOrCreate(propertyHeartBeatKey(property)) {
            HeartBeat.create().apply {
                beat()
            }
        }
    }
    val resumeAwait = AwaitState.create(false)
    val job = coroutineScope.launch(main) {
        val handler = object : ScopeHandler {
            override fun resume() {
                resumeAwait.setState(true)
            }

            override fun pause() {
                resumeAwait.setState(false)
            }

            override fun dispose() {
                coroutineContext[Job]?.cancel()
            }

        }
        stater.start(handler)
        heartBeat.onBeat {
            resumeAwait.awaitState(true)
            invokeAction(property, action)
        }
    }
    return Disposable {
        job.cancel()
    }
}


internal fun IStoreProvider.notifyAnchorPropertyChanged(
    property: KProperty<*>,
) {
    fromStore {
        val flow =
            getInstance<Channel<PropertyEvent>>(anchorPropertyEventChannelKey) ?: return@fromStore
        flow.trySend(property.asEvent())
    }

}

internal fun <T : IStoreProvider> T.registerAnchorPropertyChangedListenerImpl(
    starter: AnchorStarter = AnchorStarter,
    action: AnchorScope<T>.(T) -> Unit
): Disposable {
    val anchorPropertyEventChannel = fromStore {
        getOrCreate(anchorPropertyEventChannelKey) {
            Channel<PropertyEvent>(Channel.UNLIMITED)
        }
    }
    val anchorPropertyEventFlow = fromStore {
        getOrCreate(anchorPropertyEventsFlowKey) {
            anchorPropertyEventChannel.receiveAsFlow()
                .shareIn(coroutineScope, SharingStarted.Lazily)

        }
    }
    val scope = AnchorScopeImpl(this, anchorPropertyEventFlow, action)
    coroutineScope.launch(main) {
        starter.start(scope)
    }
    return scope

}


private fun dataSaveStateKey(storeId: String, property: KProperty<*>): String {
    return "000000_${property.name}_${storeId}_00000"
}


internal fun ISaveStateStore.dataSaveStateKey(property: KProperty<*>): String {
    return dataSaveStateKey(id, property)
}

internal fun dataPropertyKey(property: KProperty<*>): String {
    return "0ba5e5af-c5e9-4969-8164-a0c3e0c2185e_${property.name}_$\$"
}


internal fun propertyHeartBeatKey(property: KProperty<*>): String {
    return "0eada515-dc29-498b-a552-e216ee10b4f2_${property.name}_\$\$"
}

internal operator fun Disposable.plus(disposable: Disposable) = Disposable {
    dispose()
    disposable.dispose()
}


internal operator fun AnchorScopeLifecycleHandler.plus(disposable: Disposable): AnchorScopeLifecycleHandler {
    return object : AnchorScopeLifecycleHandler {
        override fun launch() {
            this@plus.launch()
        }

        override fun resume() {
            this@plus.resume()
        }

        override fun pause() {
            this@plus.pause()
        }

        override fun dispose() {
            this@plus.dispose()
            disposable.dispose()
        }

    }
}


internal fun <T, V : IStoreProvider> T.propertyValueByDelegate(
    thisRef: V,
    property: KProperty<*>
): T {
    return with(thisRef) {
        fromStore {
            val key = dataPropertyKey(property)
            var result = getInstance<T>(key)
            val contains = contains(key)
            val feature = getFeature(property)
            if (!contains && feature.hasFeature(Feature.SaveState) && this@with is ISaveStateStoreProvider
            ) {
                result = fromSaveStateStore {
                    getSavedState(dataSaveStateKey(property))
                }
            }
            val _result = result ?: this@propertyValueByDelegate
            if (!contains) {
                put(key, _result)
                notifyPropertyChanged(property)
                if (feature.hasFeature(Feature.Anchor)) {
                    notifyAnchorPropertyChanged(property)
                }
            }
            _result
        }
    }
}


internal fun <T, V : IStoreProvider> setPropertyValueByDelegate(
    thisRef: V,
    property: KProperty<*>,
    value: T
) {
    with(thisRef) {
        fromStore {
            val key = dataPropertyKey(property)
            val feature = getFeature(property)
            val last = getInstance<T>(key)
            val equals = getEqualsImpl(property)
            if (!equals.equals(last, value)) {
                put(key, value)
                if (feature.hasFeature(Feature.SaveState) && this@with is ISaveStateStoreProvider) {
                    fromSaveStateStore {
                        saveState(dataSaveStateKey(property), value)
                    }
                }
                notifyPropertyChanged(property)
                if (feature.hasFeature(Feature.Anchor))
                    notifyAnchorPropertyChanged(property)
            }

        }
    }
}


internal fun <T> T.propertyFeatureImpl(
    component: IStoreProviderComponent,
    equals: Equals<T>,
    vararg feature: Feature
): ReadWriteProperty<IStoreProviderComponent, T> {
    var defaultFeature: Feature = Feature
    feature.forEach {
        defaultFeature += it
    }
    return NotifyPropertyDelegate(
        component,
        InstanceFactory(this),
        InstanceTransfer(),
        EmptyInitializer(),
        StandardNotifier(),
        FeatureProvider(defaultFeature),
        equals
    )
}

private fun propertyFeatureKey(property: KProperty<*>): String {
    return "${propertyFeatureKey}_$\$_${property.name}_"
}

private fun propertyEqualsKey(property: KProperty<*>): String {
    return "${propertyEqualsKey}_$\$_${property.name}_"
}

internal fun IStoreProvider.getFeature(
    property: KProperty<*>,
    defaultFeature: Feature = Feature
): Feature {
    val key = propertyFeatureKey(property)
    return fromStore {
        getInstance<Feature>(key) ?: defaultFeature.also {
            put(key, it)
        }
    }
}

internal fun IStoreProvider.hasFeature(property: KProperty<*>, feature: Feature): Boolean {
    return getFeature(property).hasFeature(feature)
}

internal fun IStoreProvider.setFeatureIImpl(property: KProperty<*>, feature: Feature) {
    val key = propertyFeatureKey(property)
    fromStore {
        put(key, feature)
    }
}

internal fun IStoreProvider.setEqualsImpl(property: KProperty<*>, equals: Equals<*>) {
    val key = propertyEqualsKey(property)
    fromStore {
        put(key, equals)
    }
}

internal fun <V> IStoreProvider.getEqualsImpl(property: KProperty<V>): Equals<V> {
    val key = propertyEqualsKey(property)
    return fromStore {
        getInstance<Equals<V>>(key) ?: DefaultEquals<V>().apply {
            put(key, this)
        }
    }
}

@Synchronized
internal fun IStoreProvider.appendFeature(property: KProperty<*>, feature: Feature) {
    val key = propertyFeatureKey(property)
    fromStore {
        var value = getInstance<Feature>(key) ?: Feature
        value += feature
        put(key, value)
    }
}

@Synchronized
internal fun IStoreProvider.removeFeature(property: KProperty<*>, feature: Feature) {
    val key = propertyFeatureKey(property)
    fromStore {
        var value = getInstance<Feature>(key) ?: Feature
        value -= feature
        put(key, value)
    }
}