package com.x930073498.rstore.anchor

import androidx.collection.arrayMapOf
import com.x930073498.rstore.AnchorScope
import com.x930073498.rstore.DefaultEquals
import com.x930073498.rstore.Disposable
import com.x930073498.rstore.Equals
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.PropertyEvent
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
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

data class PropertyValue(val property: KProperty<*>, val value: Any?)

internal class PropertyAction<T : IStoreProvider, V>(
    private val property: KProperty<V>,
    private val action: V.() -> Unit,
) {
    suspend fun run(
        provider: T,
        changeProperty: List<KProperty<*>>,
        map: MutableMap<KProperty<*>, Any?>,
        isForce: Boolean,
    ): PropertyValue {
        val result = if (isForce) {
            run(provider, map[property])
        } else {
            run(provider, changeProperty, map[property])
        }
        return PropertyValue(property, result)

    }

    private suspend fun run(provider: T, changeProperty: List<KProperty<*>>, pre: Any?): Any? {
        val changedProperty = changeProperty.firstOrNull {
            it == property || it.name == property.name
        }
        return if (changedProperty != null) {
            run(provider, pre)
        } else pre

    }

    private suspend fun run(provider: T, pre: Any?): Any? {
        return when (property) {
            is KProperty0<V> -> {
                val v = property.invoke()
                withContext(provider.main) {
                    action(v)
                }
                v
            }
            is KProperty1<*, V> -> {
                property as KProperty1<T, V>
                val v = property.invoke(provider)
                withContext(provider.main) {
                    action(v)
                }
                v
            }
            else -> pre

        }
    }

}

internal class AnchorScopeImpl<T : IStoreProvider>(
    private val storeProvider: T,
    private val flow: Flow<PropertyEvent>,
    private val action: T.(AnchorScope<T>) -> Unit

) : Disposable, AnchorScope<T> {
    private var job: Job? = null
    private var isPause = true
    private val pauseChannel = Channel<Boolean>(1)
    private var isInitialized = false
    private val changedProperties = arrayListOf<KProperty<*>>()
    private val changedChannel = Channel<Int>(1, BufferOverflow.DROP_OLDEST)
    private val changedPropertyValueMap = arrayMapOf<KProperty<*>, Any?>()
    private val actions = arrayListOf<PropertyAction<T, *>>()
    private var initAction: () -> Unit = {}
    private val lock = ReentrantLock()
    private var count = 0

    override fun dispose() {
        job?.cancel()
        changedPropertyValueMap.clear()
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

    fun launch() {
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
                it.run(storeProvider, properties, changedPropertyValueMap, !isInitialized)
            }.forEach {
                changedPropertyValueMap[it.property] = it.value
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


    fun resume() {
        isPause = false
        pauseChannel.trySend(false)
    }

    fun pause() {
        isPause = true
        pauseChannel.trySend(true)

    }
}