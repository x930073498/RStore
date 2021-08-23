package com.x930073498.rstore


fun interface Equals<V> {
    fun equals(data1: V?, data2: V?): Boolean
}

class DefaultEquals<V> : Equals<V> {
    override fun equals(data1: V?, data2: V?): Boolean {
        return data1 == data2
    }

}
