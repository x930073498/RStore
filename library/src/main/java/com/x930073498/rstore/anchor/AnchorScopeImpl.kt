package com.x930073498.rstore.anchor

import com.x930073498.rstore.core.*
import com.x930073498.rstore.event.EventActionManager
import com.x930073498.rstore.internal.PropertyEvent
import com.x930073498.rstore.util.AwaitState
import com.x930073498.rstore.util.HeartBeat
import com.x930073498.rstore.util.LockList
import com.x930073498.rstore.util.awaitState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlin.reflect.KProperty



internal class AnchorScopeImpl<T : IStoreProvider>(
    private val storeProvider: T,
    private val flow: Flow<PropertyEvent>,
    private val action: AnchorScope<T>.(T) -> Unit
) : Disposable, AnchorScope<T>, AnchorScopeLifecycleHandler {
    private var job: Job? = null
    private val resumeAwaitState = AwaitState.create(false)
    private val manager = EventActionManager<T, AnchorScopeState>(storeProvider)
    private val state = AnchorScopeState(false, resumeAwaitState)

    override fun dispose() {
        manager.dispose()
        job?.cancel()
    }


    override fun launch() {
        state.isInitialized = false
        job?.cancel()
        job = with(storeProvider) {
            launchOnIO {
                var count = 0
                flow.map {
                    println("enter this line 111")
                    count++
                }
                    .onStart {
                        emit(count++)
                    }
                    .buffer(Channel.CONFLATED)
                    .collect {
                        println("enter this line 222")
                        runAction()
                    }
            }
        }
    }

    private suspend fun AnchorScopeImpl<T>.runAction() {
        with(storeProvider) {
            manager.begin()
            delay(200)
            withContext(main) {
                resumeAwaitState.awaitState(true)
                action(this@AnchorScopeImpl, this@with)
            }
            manager.end()
            resumeAwaitState.awaitState(true)
            manager.runAction(state)
            state.isInitialized = true
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