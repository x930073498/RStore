package com.x930073498.lifecycle.internal

import android.app.Activity
import android.app.Application
import androidx.collection.arrayMapOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.x930073498.lifecycle.core.Removable
import com.x930073498.lifecycle.core.ScopeEnvironment

class ScopeEnvironmentImpl(
    override val application: Application,
    override val activities: List<Activity>,
    override val fragments: List<Fragment>,
    override val processLifecycleOwner: LifecycleOwner
) : ScopeEnvironment {
    private val removableMap = arrayMapOf<Any, MutableList<DelegateRemovable>>()

    private class DelegateRemovable(
        private val removableMap: MutableMap<Any, MutableList<DelegateRemovable>>,
        private val key: Any,
        val removable: Removable
    ) : Removable {
        init {
            val list = removableMap[key] ?: arrayListOf<DelegateRemovable>().apply {
                removableMap[key] = this
            }
            list.add(this)
        }

        var isRemoved = false
        override fun remove() {
            if (isRemoved) return
            isRemoved = true
            removable.remove()
            val list = removableMap[key] ?: return
            list.remove(this)
            if (list.isEmpty()) removableMap.remove(key)
        }

    }

    private fun Removable(key: Any, action: () -> Unit): Removable {
        return DelegateRemovable(removableMap, key, object : Removable {
            override fun remove() {
                action()
            }
        })
    }


     fun Removable(activity: Activity, action: () -> Unit): Removable {
        return Removable(activity as Any, action)
    }

     fun Removable(fragment: Fragment, action: () -> Unit): Removable {
        return Removable(fragment as Any, action)
    }

     fun Removable(application: Application, action: () -> Unit): Removable {
        return Removable(application as Any, action)
    }

    internal fun disposeRemovable(any: Any) {
        removableMap.remove(any)?.apply {
            forEach {
                it.removable.remove()
            }
            clear()
        }

    }

    internal fun clearRemovable() {
        val valueIterator = removableMap.values.iterator()
        while (valueIterator.hasNext()) {
            val iterator = valueIterator.next()?.iterator() ?: continue
            while (iterator.hasNext()) {
                val removable = iterator.next()
                removable.removable.remove()
            }
        }
        removableMap.clear()
    }
}