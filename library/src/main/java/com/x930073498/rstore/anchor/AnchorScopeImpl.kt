package com.x930073498.rstore.anchor

import com.x930073498.rstore.AnchorScope
import com.x930073498.rstore.AnchorScopeLifecycleHandler
import com.x930073498.rstore.Disposable
import com.x930073498.rstore.R
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.core.fromStore
import com.x930073498.rstore.core.getOrCreate
import com.x930073498.rstore.property.PropertyEvent
import com.x930073498.rstore.property.invokeAction
import com.x930073498.rstore.property.valueFromProperty
import com.x930073498.rstore.util.LockList
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.locks.ReentrantLock
import kotlin.reflect.KProperty


internal class PropertyAction<T : IStoreProvider, V>(
    private val property: KProperty<V>,
    private val action: V.() -> Unit,
) {
    suspend fun run(provider: T, container: PropertyContainer) {
        val delegateProperty = container.getDelegateProperty(property)
        if (delegateProperty != null) {
            withContext(provider.main) {
                provider.invokeAction(property, action)
            }
            container.removeDelegateProperty(delegateProperty)
        }
    }

}

internal interface PropertyContainer : Disposable {


    fun getDelegateProperty(property: KProperty<*>): KProperty<*>?

    fun addProperty(property: KProperty<*>)

    fun removeDelegateProperty(property: KProperty<*>)


}

internal class DefaultPropertyContainer(private val globalChangedProperties: LockList<KProperty<*>>) :
    PropertyContainer {
    var isInitialized = false

    private val delegateProperties = LockList<KProperty<*>>()


    override fun getDelegateProperty(property: KProperty<*>): KProperty<*>? {
        return delegateProperties.doOnLock {
            var result =
                delegateProperties.firstOrNull { property == it || property.name == it.name }
            if (!isInitialized && result == null) {
                result = if (globalChangedProperties.isEmpty()) property else
                    globalChangedProperties.firstOrNull { property == it || property.name == it.name }
            }
            result
        }
    }

    override fun addProperty(property: KProperty<*>) {
        delegateProperties.doOnLock {
            delegateProperties.remove(property)
            delegateProperties.add(property)
        }
    }

    override fun removeDelegateProperty(property: KProperty<*>) {
        delegateProperties.remove(property)
    }

    override fun dispose() {
        delegateProperties.clear()
    }

}


internal class AnchorScopeImpl<T : IStoreProvider>(
    private val storeProvider: T,
    private val flow: Flow<PropertyEvent>,
    private val globalChangedProperties: LockList<KProperty<*>>,
    private val action: T.(AnchorScope<T>) -> Unit

) : Disposable, AnchorScope<T>, AnchorScopeLifecycleHandler {


    private var job: Job? = null
    private var isPause = true
    private val pauseChannel = Channel<Boolean>(1)
    private var isInitialized = false
    private val container = DefaultPropertyContainer(globalChangedProperties)
    private val changedChannel = Channel<Int>(1, BufferOverflow.DROP_OLDEST)
    private val actions = arrayListOf<PropertyAction<T, *>>()
    private var initAction: () -> Unit = {}
    private var count = 0


    override fun dispose() {
        job?.cancel()
        container.dispose()
    }

    private fun pushProperty(property: KProperty<*>) {
        container.addProperty(property)
        if (!globalChangedProperties.contains(property)) {
            globalChangedProperties.add(property)
        }
    }


    override fun launch() {
        isInitialized = false
        job?.cancel()
        job = with(storeProvider) {
            coroutineScope.launch {
                async(io) {
                    flow.collect {
                        pushProperty(it.property)
                        changedChannel.send(count++)
                    }
                }.start()
                async(io) {
                    for (value in changedChannel) {
                        runAction()
                    }
                }.start()
                changedChannel.send(count++)
            }

        }
    }

    private suspend fun AnchorScopeImpl<T>.runAction() {
        with(storeProvider) {
            awaitNotPause()
            withContext(main) {
                action(this@AnchorScopeImpl)
            }
            if (!isInitialized) {
                withContext(main) {
                    initAction()
                }
                initAction = {}
            }
            actions.map {
                it.run(this, container)
            }
            actions.clear()
            isInitialized = true
            container.isInitialized = true
        }
    }


    override fun onInitialized(action: () -> Unit) {
        if (isInitialized) return
        initAction = action
    }

    override fun <V> stareAt(property: KProperty<V>, action: V.() -> Unit) {
        with(storeProvider) {
            valueFromProperty(property)
        }
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