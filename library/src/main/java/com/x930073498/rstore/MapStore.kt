package com.x930073498.rstore

import com.x930073498.rstore.core.IStore
import java.io.Closeable
import java.util.concurrent.locks.ReentrantLock

class MapStore : IStore {
    private val map = mutableMapOf<String, Any?>()
    private val lock = ReentrantLock()
    private fun <R> doLock(action: () -> R): R {
        lock.lock()
        val result: R = action()
        lock.unlock()
        return result
    }

    override fun remove(key: String) {
        doLock {
            map.remove(key).finish()

        }
    }

    override fun put(key: String, value: Any?) {
        doLock {
            map[key] = value
        }
    }

    override fun get(key: String): Any? {
        return doLock {
            map[key]
        }
    }

    override fun clear() {
        doLock {
            map.values.forEach {
                it.finish()
            }
            map.clear()
        }
    }

    private fun Any?.finish() {
        runCatching {
            if (this is Closeable) close()
            else if (this is Disposable) dispose()
        }
    }
}