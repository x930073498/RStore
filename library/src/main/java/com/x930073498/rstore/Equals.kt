package com.x930073498.rstore

fun interface Equals<V> {
    fun equals(data1: V?, data2: V?): Boolean

}

class DefaultEquals<V> : Equals<V> {
    override fun equals(data1: V?, data2: V?): Boolean {
        return data1 == data2
    }

}

class ListEquals<V, T : List<V>>(private val equals: Equals<V>) : Equals<T> {
    override fun equals(data1: T?, data2: T?): Boolean {
        return if (data1 != null) {
            if (data2 == null) false
            else {
                if (data1 === data2) true
                else if (data1.size != data2.size) false
                else {
                    for (i in data1.indices) {
                        if (!equals.equals(data1[i], data2[i])) return false
                    }
                    true
                }
            }
        } else {
            data2 == null
        }
    }

}