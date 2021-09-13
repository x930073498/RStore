package com.x930073498.rstore.util

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlin.coroutines.coroutineContext

interface AwaitState<T> {
    companion object {
        fun <T> create(defaultState: T): AwaitState<T> {
            return AwaitStateImpl(defaultState)
        }
    }

    fun setState(state: T)

    val state:T

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
        flow.first { predicate(it) }
    }

    override val state: T
        get() = flow.value


}


