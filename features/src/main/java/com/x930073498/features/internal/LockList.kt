package com.x930073498.features.internal

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

interface LockList<T> {
    companion object {
        fun <T> create(): LockList<T> {
            return LockListImpl(ReentrantLock(), arrayListOf())
        }
    }

    val lock: Lock
    val list: MutableList<T>

    private class LockListImpl<T>(override val lock: Lock, override val list: MutableList<T>) :
        LockList<T>
}


fun <T, R> LockList<T>.doOnLock(action: MutableList<T>.() -> R): R {
    lock.lock()
    val result = action(list)
    lock.unlock()
    return result
}

fun <T> LockList<T>.forEach(action: T.() -> Unit) {
    doOnLock {
        forEach(action)
    }
}