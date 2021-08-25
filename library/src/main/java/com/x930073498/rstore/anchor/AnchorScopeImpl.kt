package com.x930073498.rstore.anchor

import com.x930073498.rstore.AnchorScope
import com.x930073498.rstore.AnchorScopeLifecycleHandler
import com.x930073498.rstore.Disposable
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.PropertyEvent
import com.x930073498.rstore.property.invokeAction
import com.x930073498.rstore.property.valueFromProperty
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import java.util.concurrent.locks.ReentrantLock
import kotlin.reflect.KProperty

data class PropertyValue(val property: KProperty<*>, val value: Any?)

internal class PropertyAction<T : IStoreProvider, V>(
    private val property: KProperty<V>,
    private val action: V.() -> Unit,
) {
    suspend fun run(
        provider: T,
        changeProperty: List<KProperty<*>>,
        isFirstRun: Boolean,
    ) {
        if (isFirstRun) {
            provider.valueFromProperty(property)
        } else {
            run(provider, changeProperty)
        }
    }

    private suspend fun run(provider: T, changeProperty: List<KProperty<*>>) {
        val changedProperty = changeProperty.firstOrNull {
            it == property || it.name == property.name
        }
        if (changedProperty != null) {
            run(provider)
        }

    }

    private suspend fun run(provider: T) {
        withContext(provider.main) {
            provider.invokeAction(property, action)
        }
    }

}


internal class AnchorScopeImpl<T : IStoreProvider>(
    private val storeProvider: T,
    private val flow: Flow<PropertyEvent>,
    private val action: T.(AnchorScope<T>) -> Unit

) : Disposable, AnchorScope<T>,AnchorScopeLifecycleHandler {
    private var job: Job? = null
    private var isPause = true
    private val pauseChannel = Channel<Boolean>(1)
    private var isInitialized = false
    private val changedProperties = arrayListOf<KProperty<*>>()
    private val changedChannel = Channel<Int>(1, BufferOverflow.DROP_OLDEST)
    private val actions = arrayListOf<PropertyAction<T, *>>()
    private var initAction: () -> Unit = {}
    private val lock = ReentrantLock()
    private var count = 0

    override fun dispose() {
        job?.cancel()
        changedProperties.clear()
    }

    private fun pushProperty(property: KProperty<*>) {
        lock.lock()
        changedProperties.remove(property)
        changedProperties.add(property)
        lock.unlock()
        changedChannel.trySend(count++)
    }

    private fun getChangedPropertiesSnap(): List<KProperty<*>> {
        lock.lock()
        val result = changedProperties.toList()
        changedProperties.clear()
        lock.unlock()
        return result
    }

    override fun launch() {
        isInitialized = false
        job?.cancel()
        job = with(storeProvider) {
            coroutineScope.launch {
                async(io) {
                    changedChannel.trySend(count++)
                    flow.collect {
                        pushProperty(it.property)
                    }
                }.start()
                async(io) {
                    for (value in changedChannel) {
                        runAction()
                    }
                }.start()
            }

        }
    }

    private suspend fun AnchorScopeImpl<T>.runAction() {
        with(storeProvider) {
            awaitNotPause()
            withContext(main) {
                action(this@AnchorScopeImpl)
            }
            val properties = getChangedPropertiesSnap()
            if (!isInitialized) {
                withContext(main) {
                    initAction()
                }
                initAction = {}
            }
            actions.map {
                it.run(storeProvider, properties, !isInitialized)
            }
            actions.clear()
            isInitialized = true
        }
    }


    override fun onInitialized(action: () -> Unit) {
        if (isInitialized) return
        initAction = action
    }

    override fun <V> stareAt(property: KProperty<V>, action: V.() -> Unit) {
        actions.add(
            PropertyAction(
                property,
                action
            )
        )
    }

    private suspend fun awaitNotPause() {
        if (!isPause) return
        while (select {
                pauseChannel.onReceive {
                    it
                }
            }) {
            //do Loop
        }
    }


    override fun resume() {
        isPause = false
        pauseChannel.trySend(false)
    }

    override fun pause() {
        isPause = true
        pauseChannel.trySend(true)

    }
}