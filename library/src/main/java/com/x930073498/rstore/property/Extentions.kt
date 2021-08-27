package com.x930073498.rstore.property

import androidx.lifecycle.*
import com.x930073498.rstore.*
import com.x930073498.rstore.anchor.AnchorScopeImpl
import com.x930073498.rstore.core.*
import com.x930073498.rstore.util.AwaitState
import com.x930073498.rstore.util.HeartBeat
import com.x930073498.rstore.util.LockList
import com.x930073498.rstore.util.awaitState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1


private const val anchorPropertyEventChannelKey = "1306d9dd-5824-4341-af54-d45265fc2a1e"
private const val anchorPropertyEventsFlowKey = "4dd1aea6-5f82-43be-bddd-d538b5961a38"
private const val globalChangePropertiesKey = "eb3dae31-ff93-43a4-aa8c-51492add1f01"

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
    valueFromProperty(kProperty)?.apply(action)
}

internal fun IStoreProvider.notifyPropertyChanged(property: KProperty<*>) {
    val heartBeat = fromStore {
        getInstance<HeartBeat>(dataPropertyKey(property))
    } ?: return
    heartBeat.beat()
}


internal suspend fun <T : IStoreProvider, V> T.awaitUntilImpl(
    property: KProperty<V>,
    predicate: suspend V.() -> Boolean
) {
    val heartBeat = fromStore {
        getOrCreate(dataPropertyKey(property)) {
            HeartBeat.create()
        }
    }
    heartBeat.onBeat {
        val value = valueFromProperty(property) as V
        if (predicate.invoke(value)) dispose()
    }

}

internal fun <T : IStoreProvider, V> T.registerPropertyChangedListenerImpl(
    property: KProperty<V>,
    lifecycleOwner: LifecycleOwner,
    action: V.() -> Unit
): Disposable {
    val heartBeat = fromStore {
        getOrCreate(dataPropertyKey(property)) {
            HeartBeat.create()
        }
    }
    val resumeAwait = AwaitState.create(false)
    val job = coroutineScope.launch(main) {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                resumeAwait.setState(false)
            }

            override fun onResume(owner: LifecycleOwner) {
                resumeAwait.setState(true)
            }
        })
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
        val flow = getInstance<Channel<PropertyEvent>>(anchorPropertyEventChannelKey)
        flow?.trySend(property.asEvent())
    }

}

internal fun <T : IStoreProvider> T.registerAnchorPropertyChangedListenerImpl(
    starter: AnchorStarter = AnchorStarter,
    action: T.(AnchorScope<T>) -> Unit
): Disposable {
    val globalChangeProperties = fromStore {
        getOrCreate(globalChangePropertiesKey) {
            LockList<KProperty<*>>()
        }
    }
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
    val scope = AnchorScopeImpl(this, anchorPropertyEventFlow, globalChangeProperties, action)
    coroutineScope.launch(main) {
        starter.start(scope)
    }
    return scope

}


internal fun dataSaveStateKey(storeId: String, property: KProperty<*>): String {
    return "000000_${property.name}_${storeId}_00000"
}

internal fun dataPropertyKey(property: KProperty<*>): String {
    return "0ba5e5af-c5e9-4969-8164-a0c3e0c2185e_${property.name}_$\$"
}

internal operator fun Disposable.plus(disposable: Disposable) = Disposable {
    dispose()
    disposable.dispose()
}


internal fun <T : IStoreProvider> AnchorScope<T>.stareAtImpl(
    vararg property: KProperty<*>,
    action: () -> Unit
) {
    property.forEach { stareAt(it) { action() } }
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