package com.x930073498.rstore

import androidx.lifecycle.SavedStateHandle
import com.x930073498.rstore.core.ISaveStateStore
import java.util.*
import java.util.concurrent.locks.ReentrantLock

private const val saveStateIdKey = "ba2aafc1-d745-44e1-b7f4-c3a472d3fc0c"

class SaveStateHandleStore(private val handle: SavedStateHandle) : ISaveStateStore {
    override val id: String =
        handle.get<String>(saveStateIdKey) ?: (UUID.randomUUID().toString().apply {
            handle.set(saveStateIdKey, this)
        })


    private val lock = ReentrantLock()
    private fun <R> doLock(block: () -> R): R {
        lock.lock()
        val result = block()
        lock.unlock()
        return result
    }

    override fun remove(key: String) {
        doLock {
            handle.remove<Any?>(key)
        }
    }

    override fun put(key: String, value: Any?) {
        doLock {
            handle.set(key, value)
        }
    }

    override fun get(key: String): Any? {
        return doLock {
            handle.get(key)
        }
    }

    override fun clear() {
        doLock {
            handle.keys().forEach {
                handle.remove<Any?>(it)
            }
        }
    }
}