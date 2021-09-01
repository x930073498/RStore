package com.x930073498.rstore.property

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.x930073498.rstore.core.StoreComponent
import com.x930073498.rstore.core.defaultLifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


interface LazyFieldHandle : (KProperty<*>) -> LazyField {
    fun clear()
}

class LazyField internal constructor(
    val property: KProperty<*>,
    private val handle: LazyFieldHandle
) :
    LazyFieldHandle by handle {
    fun bindLifecycle(lifecycleOwner: LifecycleOwner?) {
        if (lifecycleOwner == null) return
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    clear()
                }
            })
        }
    }
}


fun <T : StoreComponent, V> T.lazyField(factory: T.(LazyField) -> V) =
    lazyField(defaultLifecycleOwner, factory)


fun <T, V> lazyField(
    lifecycleOwner: LifecycleOwner? = null,
    factory: T.(LazyField) -> V
): ReadOnlyProperty<T, V> =
    run {
        object : ReadOnlyProperty<T, V> {
            @Volatile
            private var value: V? = null
            val handle = object : LazyFieldHandle {
                override fun clear() {
                    value = null
                }

                override fun invoke(p1: KProperty<*>): LazyField {
                    return LazyField(p1, this).apply { bindLifecycle(lifecycleOwner) }
                }
            }

            override fun getValue(thisRef: T, property: KProperty<*>): V {
                return synchronized(this) {
                    value ?: factory(thisRef, handle(property)).apply {
                        value = this
                    }
                }
            }

        }

    }

