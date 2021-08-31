package com.x930073498.rstore.property

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty



fun <T, V> lazyField(factory: T.(KProperty<*>) -> V): ReadOnlyProperty<T, V> =
    run {
        var value: V? = null
        ReadOnlyProperty { thisRef, property ->
            value ?: factory(thisRef, property).apply {
                value = this
            }
        }
    }

