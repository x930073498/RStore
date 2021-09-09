package com.x930073498.rstore.util

import com.x930073498.rstore.core.Disposable
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

interface HeartBeat {

    companion object {
        fun create(): HeartBeat {
            return HeartBeatImpl()
        }
    }

    fun beat()

    suspend fun <R> onBeat(action: suspend HeartBeatHandle.() -> R): R
}

fun HeartBeat.asFlow(): Flow<Unit> {
    return flow {
        onBeat {
            emit(Unit)
        }
    }

}

fun interface HeartBeatHandle : Disposable {
}


private class HeartBeatImpl : HeartBeat {

    private val count = AtomicLong(0)

    private val flow = MutableSharedFlow<Long>(1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun beat() {
        flow.tryEmit(count.incrementAndGet())
    }

    override suspend fun <R> onBeat(action: suspend HeartBeatHandle.() -> R): R {
        val awaitState = AwaitState.create(true)
        val handle = HeartBeatHandle {
            awaitState.setState(false)
        }
        return flow.map {
            action(handle)
        }.first { !awaitState.state }
    }


}