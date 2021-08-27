package com.x930073498.rstore.util

import com.x930073498.rstore.core.Disposable
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.selects.select

interface AwaitState<T>  {


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
    private val flow = MutableStateFlow(defaultState)

    override fun setState(state: T) {
        flow.tryEmit(state)
    }

    override suspend fun awaitState(predicate: T.() -> Boolean) {
        if (predicate(flow.value)) return
        flow.filter { predicate(it) }.single()
    }




}


