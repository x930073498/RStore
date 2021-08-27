package com.x930073498.rstore.util

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.selects.select

interface AwaitState<T> {


    companion object {
        fun <T> create(defaultState: T): AwaitState<T> {
            return AwaitStateImpl(defaultState)
        }
    }


    fun setState(state: T)

    suspend fun awaitState(predicate: T.() -> Boolean)

}


suspend fun <T> AwaitState<T>.awaitState(state: T) {
    awaitState {
        this == state
    }
}

private class AwaitStateImpl<T>(defaultState: T) : AwaitState<T> {
    private val channel = Channel<T>(1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private var currentState: T = defaultState
    override fun setState(state: T) {
        currentState = state
        channel.trySend(state)
    }

    override suspend fun awaitState(predicate: T.() -> Boolean) {
        if (predicate(currentState)) return
        while (select {
                channel.onReceive {
                    !predicate(it)
                }
            }) {
            // do loop
        }

    }


}


