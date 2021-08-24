package com.x930073498.rstore.property

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.x930073498.rstore.*
import com.x930073498.rstore.anchor.AnchorScopeImpl
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.core.fromStore
import com.x930073498.rstore.core.getInstance
import com.x930073498.rstore.core.getOrCreate
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1


private const val anchorPropertyEventChannelKey = "1306d9dd-5824-4341-af54-d45265fc2a1e"
private const val anchorPropertyEventsFlowKey = "4dd1aea6-5f82-43be-bddd-d538b5961a38"
private const val anchorStoreComponentKey = "87e64dd9-6d9b-415f-b10b-5bd04d04dbb3"

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
    val flow = fromStore {
        getInstance<MutableStateFlow<PropertyEvent>>(dataPropertyKey(property))
    } ?: return
    flow.tryEmit(property.asEvent())
}


suspend fun <T : IStoreProvider, V> T.awaitUntil(
    property: KProperty<V>,
    predicate: suspend V.() -> Boolean
) {
    val flow = fromStore {
        getOrCreate(dataPropertyKey(property)) {
            MutableStateFlow(property.asEvent())
        }
    }
    flow.filter {
        val value: V = if (property is KProperty0) {
            property.invoke()
        } else {
            property as KProperty1<T, V>
            property.invoke(this)
        }
        predicate.invoke(value)
    }.first()

}

internal fun <T : IStoreProvider, V> T.registerPropertyChangedListenerImpl(
    property: KProperty<V>,
    lifecycleOwner: LifecycleOwner? = null,
    action: V.() -> Unit
): Disposable {
    val flow = fromStore {
        getOrCreate(dataPropertyKey(property)) {
            MutableStateFlow(property.asEvent())
        }
    }

    if (lifecycleOwner == null) {
        val job = coroutineScope.launch(main) {
            flow.collect {
                invokeAction(property, action)
            }
        }
        return Disposable {
            job.cancel()
        }
    } else {
        val liveData = flow.asLiveData(main)
        val observer = Observer<Any?> {
            invokeAction(property, action)
        }
        val job = coroutineScope.launch(main) {
            liveData.observe(lifecycleOwner, observer)
        }
        return Disposable {
            job.cancel()
            liveData.removeObserver(observer)
        }
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
    storeComponent: StoreComponent,
    lifecycleOwner: LifecycleOwner = storeComponent,
    action: T.(AnchorScope<T>) -> Unit
): Disposable {

    val componentKey = anchorStoreComponentKey + "code_${storeComponent.hashCode()}_$\$"
    val anchorScopeStore = fromStore {
        getOrCreate(componentKey) {
            MapStore()
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
        }
    }.shareIn(coroutineScope, SharingStarted.Lazily)
    val id = UUID.randomUUID().toString()
    val scope =
        AnchorScopeImpl(this, anchorPropertyEventFlow, action)
    coroutineScope.launch(main) {
        storeComponent.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                fromStore {
                    remove(componentKey)
                    anchorScopeStore.dispose()
                }
            }
        })
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                scope.launch()
            }

            override fun onPause(owner: LifecycleOwner) {
                scope.pause()
            }

            override fun onResume(owner: LifecycleOwner) {
                scope.resume()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                scope.dispose()
                anchorScopeStore.remove(id)
            }
        })
        anchorScopeStore.put(id, scope)
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