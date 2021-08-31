package com.x930073498.rstore.core

import com.x930073498.rstore.internal.dataPropertyKey
import com.x930073498.rstore.internal.notifyAnchorPropertyChanged
import com.x930073498.rstore.internal.notifyPropertyChanged
import kotlin.reflect.KProperty

interface IStoreProviderComponent : IStoreProvider, ISaveStateStoreProvider {

//    operator fun <T, V : IStoreProviderComponent> T.getValue(
//        thisRef: V,
//        property: KProperty<*>
//    ): T {
//        return fromStore {
//            this.getInstance<T>(dataPropertyKey(property))
//        } ?: this
//    }
//
//    operator fun <T, V : IStoreProviderComponent> T.setValue(
//        thisRef: V,
//        property: KProperty<*>,
//        value: T
//    ) {
//        fromStore {
//            val key = dataPropertyKey(property)
//            val last = getInstance<T>(key)
//            if (last != value) {
//                put(dataPropertyKey(property), value)
//                notifyPropertyChanged(property)
//                notifyAnchorPropertyChanged(property)
//            }
//
//        }
//    }
}

operator fun <T, V : IStoreProviderComponent> T.getValue(
    thisRef: V,
    property: KProperty<*>
): T {
    return thisRef.fromStore {
        this.getInstance<T>(dataPropertyKey(property))
    } ?: this
}

operator fun <T, V : IStoreProviderComponent> T.setValue(
    thisRef: V,
    property: KProperty<*>,
    value: T
) {
    with(thisRef) {
        fromStore {
            val key = dataPropertyKey(property)
            val last = getInstance<T>(key)
            if (last != value) {
                put(dataPropertyKey(property), value)
                notifyPropertyChanged(property)
                notifyAnchorPropertyChanged(property)
            }

        }
    }

}