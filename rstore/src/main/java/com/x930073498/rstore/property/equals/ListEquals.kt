package com.x930073498.rstore.property.equals

import com.x930073498.rstore.core.Equals

class ListEquals<V, T : List<V>>(private val equals: Equals<V>) : Equals<T> {
    override fun equals(data1: T?, data2: T?): Boolean {
        return if (data1 != null) {
            if (data2 == null) false
            else {
                when {
                    data1 === data2 -> true
                    data1.size != data2.size -> false
                    else -> {
                        for (i in data1.indices) {
                            if (!equals.equals(data1[i], data2[i])) return false
                        }
                        true
                    }
                }
            }
        } else {
            data2 == null
        }
    }

}

