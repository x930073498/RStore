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

    suspend fun onBeat(action: suspend HeartBeatHandle.() -> Unit)
}

fun interface HeartBeatHandle : Disposable {
}


private class HeartBeatImpl : HeartBeat {

    private val count = AtomicLong(0)

    private val flow = MutableStateFlow(count.get())

    override fun beat() {
        flow.tryEmit(count.incrementAndGet())
    }

    override suspend fun onBeat(action: suspend HeartBeatHandle.() -> Unit) {
        val awaitState = AwaitState.create(true)
        val handle = HeartBeatHandle {
            awaitState.setState(false)
        }
        flow.buffer(Channel.CONFLATED).map {
            action(handle)
        }.first { !awaitState.state }
    }


}