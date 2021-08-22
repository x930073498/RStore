package com.x930073498.rstore.property

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.x930073498.rstore.*
import com.x930073498.rstore.anchor.AnchorScopeImpl
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.core.fromStore
import com.x930073498.rstore.core.getInstance
import com.x930073498.rstore.core.getOrCreate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1


private const val anchorPropertiesKey = "930d32dc-f133-4303-ac09-e1b062962345"
private const val anchorPropertyEventsKey = "1306d9dd-5824-4341-af54-d45265fc2a1e"
private const val anchorStoreComponentKey = "87e64dd9-6d9b-415f-b10b-5bd04d04dbb3"

internal data class PropertyEvent(
    val property: KProperty<*>,
    val id: String = UUID.randomUUID().toString()
)

private fun KProperty<*>.asEvent() = PropertyEvent(this)

internal fun IStoreProvider.notifyPropertyChanged(property: KProperty<*>) {
    val flow = fromStore {
        getInstance<MutableStateFlow<PropertyEvent>>(dataPropertyKey(property))
    } ?: return
    flow.tryEmit(property.asEvent())
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
    fun doAction() {
        if (property is KProperty0<V>) {
            action(property.invoke())
        } else if (property is KProperty1<*, *>) {
            (property as? KProperty1<T, V>)?.let {
                action(it.invoke(this))
            }
        }
    }
    if (lifecycleOwner == null) {
        val job = coroutineScope.launch(main) {
            flow.collect {
                doAction()
            }
        }
        return Disposable {
            job.cancel()
        }
    } else {
        val liveData = flow.asLiveData(main)
        val observer = Observer<Any?> {
            doAction()
        }
        liveData.observe(lifecycleOwner, observer)
        return Disposable {
            liveData.removeObserver(observer)
        }
    }
}


internal fun IStoreProvider.notifyAnchorPropertyChanged(property: KProperty<*>) {
    fromStore {
        val flow = getInstance<MutableSharedFlow<PropertyEvent>>(anchorPropertyEventsKey)
        flow?.tryEmit(property.asEvent())
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
    val anchorPropertyEventFlow = fromStore {
        getOrCreate(anchorPropertyEventsKey) {
            MutableSharedFlow<PropertyEvent>(1)
        }
    }
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


internal fun dataSaveStateKey(property: KProperty<*>): String {
    return "000000_${property.name}_00000"
}

internal fun dataPropertyKey(property: KProperty<*>): String {
    return "0ba5e5af-c5e9-4969-8164-a0c3e0c2185e_${property.name}_$\$"
}

internal operator fun Disposable.plus(disposable: Disposable) = Disposable {
    dispose()
    disposable.dispose()
}

private fun Disposable.bindLifecycle(lifecycle: Lifecycle) = apply {
    runOnMain {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                dispose()
            }
        })
    }
}

private fun runOnMain(block: () -> Unit) {
    if (Looper.getMainLooper() == Looper.myLooper()) {
        block()
        return
    }
    Handler(Looper.getMainLooper()).post { block() }
}