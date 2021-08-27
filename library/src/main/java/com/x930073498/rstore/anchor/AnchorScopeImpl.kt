package com.x930073498.rstore.anchor

import com.x930073498.rstore.core.*
import com.x930073498.rstore.event.EventActionManager
import com.x930073498.rstore.internal.PropertyEvent
import com.x930073498.rstore.util.AwaitState
import com.x930073498.rstore.util.HeartBeat
import com.x930073498.rstore.util.LockList
import com.x930073498.rstore.util.awaitState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KProperty


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
    private val action: AnchorScope<T>.(T) -> Unit
) : Disposable, AnchorScope<T>, AnchorScopeLifecycleHandler {
    private var job: Job? = null
    private val resumeAwaitState = AwaitState.create(false)
    private val manager = EventActionManager<T, AnchorScopeState>(storeProvider)
    private val container = DefaultPropertyContainer(globalChangedProperties)
    private val state = AnchorScopeState(false, container)
    private val changedHeartBeat = HeartBeat.create()

    override fun dispose() {
        container.dispose()
        manager.dispose()
        job?.cancel()
    }

    private fun pushProperty(property: KProperty<*>) {
        container.addProperty(property)
        if (!globalChangedProperties.contains(property)) {
            globalChangedProperties.add(property)
        }
    }


    override fun launch() {
        state.isInitialized = false
        job?.cancel()
        job = with(storeProvider) {
            coroutineScope.launch {
                async(io) {
                    flow.collect {
                        pushProperty(it.property)
                        changedHeartBeat.beat()
                    }
                }.start()
                async(io) {
                    changedHeartBeat.onBeat {
                        runAction()
                    }
                }.start()
                changedHeartBeat.beat()
            }
        }
    }

    private suspend fun AnchorScopeImpl<T>.runAction() {
        with(storeProvider) {
            manager.begin()
            withContext(main) {
                resumeAwaitState.awaitState(true)
                action(this@AnchorScopeImpl, this@with)
            }
            manager.end()
            withContext(main) {
                resumeAwaitState.awaitState(true)
                manager.runAction(state)
            }
            state.isInitialized = true
            container.isInitialized = true
        }
    }


    override fun resume() {
        resumeAwaitState.setState(true)
    }

    override fun pause() {
        resumeAwaitState.setState(false)
    }

    override fun pushAction(eventAction: AnchorStateEventAction<T>) {
        manager.pushEventAction(eventAction)
    }


}