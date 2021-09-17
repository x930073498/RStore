package com.x930073498.rstore.util

import java.util.concurrent.locks.Lock

interface LockProvider {
    val lock: Lock
}

fun <T : LockProvider, R> T.doLock(action: T.() -> R): R {
    lock.lock()
    return try {
        action(this)
    } finally {
        lock.unlock()
    }
}