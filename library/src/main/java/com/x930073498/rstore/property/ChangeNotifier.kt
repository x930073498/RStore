package com.x930073498.rstore.property

import com.x930073498.rstore.core.IStoreProvider
import kotlin.reflect.KProperty


 interface ChangeNotifier<T : IStoreProvider, Data, Source> {
    fun T.notify(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, Source>,
        data: Data? = null,
        source: Source? = null
    )
}