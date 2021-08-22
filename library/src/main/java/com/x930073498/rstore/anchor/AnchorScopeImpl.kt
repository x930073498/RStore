package com.x930073498.rstore.anchor

import androidx.collection.arrayMapOf
import com.x930073498.rstore.AnchorScope
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
import java.util.concurrent.locks.ReentrantLock
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1


internal class PropertyAction<T : IStoreProvider, V>(
    private val property: KProperty<V>,
    private val map: MutableMap<KProperty<*>, Any?>,
    private val equals: Equals<V>,
    private val action: V.() -> Unit,
) {
    fun run(
        provider: T,
        changeProperty: List<KProperty<*>>,
        isForce: Boolean,
    ) {
        return if (isForce) {
            run(provider, map[property])
        } else {
            run(provider, changeProperty)
        }

    }

    private fun run(provider: T, changeProperty: List<KProperty<*>>) {
        val changedProperty = changeProperty.firstOrNull { it.name == property.name }
        if (changedProperty != null) {
            run(provider, map[property])
        }
    }

    private fun run(provider: T, pre: Any?) {
        when (property) {
            is KProperty0<V> -> {
                val v = property.invoke()
                if (!equals.equals(v, pre as? V)) {
                    action(v)
                    map[property] = v
                }
            }
            is KProperty1<*, V> -> {
                property as KProperty1<T, V>
                val v = property.invoke(provider)
                if (!equals.equals(v, pre as? V)) {
                    action(v)
                    map[property] = v
                }
            }

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

    private fun pushProperty(event: PropertyEvent) {
        lock.lock()
        changedProperties.remove(event.property)
        changedProperties.add(event.property)
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
                        println("enter this line property=$it")
                        pushProperty(it)
                    }
                }.start()
                async(main) {
                    for (value in changedChannel) {
                        awaitNotPause()
                        action(this@AnchorScopeImpl)
                        val properties = getChangedPropertiesSnap()
                        if (!isInitialized) {
                            initAction()
                            initAction = {}
                        }
                        actions.forEach {
                            it.run(storeProvider, properties, !isInitialized)
                        }
                        actions.clear()
                        isInitialized = true
                    }
                }.start()
            }

        }
    }


    override fun onInitialized(action: () -> Unit) {
        if (isInitialized) return
        initAction = action
    }

    override fun <V> stareAt(property: KProperty<V>, equals: Equals<V>, action: V.() -> Unit) {
        actions.add(
            PropertyAction(
                property,
                changedPropertyValueMap,
                equals,
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