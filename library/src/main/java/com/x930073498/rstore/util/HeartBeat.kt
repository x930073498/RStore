package com.x930073498.rstore.util

import com.x930073498.rstore.Disposable
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicInteger

interface HeartBeat {

    companion object {
        fun create(): HeartBeat {
            return HeartBeatImpl()
        }
    }

    fun beat()

    suspend fun onBeat(action: suspend HeartBeatHandle.() -> Unit)
}

fun interface HeartBeatHandle : Disposable


private class HeartBeatImpl : HeartBeat {

    private val count = AtomicInteger(0)

    private val flow = MutableSharedFlow<Int>(1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun beat() {
        flow.tryEmit(count.incrementAndGet())
    }

    override suspend fun onBeat(action: suspend HeartBeatHandle.() -> Unit) {
        var enable = true
        val handle = HeartBeatHandle {
            enable = false
        }
        flow.map {
            action(handle)
        }.first { !enable }
    }


}