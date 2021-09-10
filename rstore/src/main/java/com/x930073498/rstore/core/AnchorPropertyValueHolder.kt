package com.x930073498.rstore.core

import com.x930073498.rstore.core.Disposable
import com.x930073498.rstore.util.LockProvider
import com.x930073498.rstore.util.doLock
import java.util.*
import java.util.concurrent.locks.ReentrantLock

class AnchorPropertyValueHolder : LockProvider, Disposable {
    override val lock = ReentrantLock()
    private val map = mutableMapOf<String, Any?>()


    fun setPropertyValue(name: String, value: Any?) {
        doLock {
            map[name] = value
        }
    }

    fun hasPropertyValue(name: String): Boolean {
        return map.containsKey(name)
    }

    fun <T> getPropertyValue(name: String): T? {
        return map[name] as? T
    }

    override fun dispose() {
        doLock {
            map.clear()
        }
    }

}