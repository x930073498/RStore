package com.x930073498.rstore.util

import java.util.concurrent.locks.ReentrantLock

class LockList<T>(private val list: MutableList<T> = arrayListOf()) : MutableList<T> {
    private val lock = ReentrantLock()
    fun <R> doOnLock(action: MutableList<T>.() -> R): R {
        lock.lock()
        val result = action(list)
        lock.unlock()
        return result
    }

    override val size: Int
        get() = doOnLock { size }

    override fun contains(element: T): Boolean {
        return doOnLock {
            contains(element)
        }
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return doOnLock {
            containsAll(elements)
        }
    }

    override fun get(index: Int): T {
        return doOnLock {
            get(index)
        }
    }

    override fun indexOf(element: T): Int {
        return doOnLock {
            indexOf(element)
        }
    }

    override fun isEmpty(): Boolean {
        return doOnLock {
            isEmpty()
        }
    }

    override fun iterator(): MutableIterator<T> {
        return doOnLock {
            iterator()
        }
    }

    override fun lastIndexOf(element: T): Int {
        return doOnLock {
            lastIndexOf(element)
        }
    }

    override fun listIterator(): MutableListIterator<T> {
        return doOnLock {
            listIterator()
        }
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        return doOnLock {
            listIterator(index)
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        return doOnLock {
            subList(fromIndex, toIndex)
        }
    }

    override fun add(element: T): Boolean {
        return doOnLock {
            add(element)
        }
    }

    override fun add(index: Int, element: T) {
        doOnLock {
            add(index, element)
        }
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        return doOnLock {
            addAll(index, elements)
        }
    }

    override fun addAll(elements: Collection<T>): Boolean {
        return doOnLock {
            addAll(elements)
        }
    }

    override fun clear() {
        doOnLock {
            clear()
        }
    }

    override fun remove(element: T): Boolean {
        return doOnLock {
            remove(element)
        }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return doOnLock {
            retainAll(elements)
        }
    }

    override fun removeAt(index: Int): T {
        return doOnLock {
            removeAt(index)
        }
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        return doOnLock {
            retainAll(elements)
        }
    }

    override fun set(index: Int, element: T): T {
        return doOnLock {
            set(index, element)
        }
    }
}