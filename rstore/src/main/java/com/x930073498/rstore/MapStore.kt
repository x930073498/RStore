package com.x930073498.rstore

import com.x930073498.rstore.core.Disposable
import com.x930073498.rstore.core.IStore
import java.io.Closeable
import java.util.concurrent.locks.ReentrantLock

class MapStore : IStore {
    private val map = mutableMapOf<Any, Any?>()
    private val lock = ReentrantLock()
    private fun <R> doLock(action: () -> R): R {
        lock.lock()
        return try {
            action()
        } finally {
            lock.unlock()
        }
    }

    override fun remove(key: Any) {
        doLock {
            map.remove(key).finish()

        }
    }

    override fun put(key: Any, value: Any?) {
        doLock {
            map[key] = value
        }
    }

    override fun get(key: Any): Any? {
        return doLock {
            map[key]
        }
    }

    override fun contains(key: Any): Boolean {
        return doLock {
            map.containsKey(key)
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