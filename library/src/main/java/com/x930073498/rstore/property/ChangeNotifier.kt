package com.x930073498.rstore.property

import com.x930073498.rstore.core.IStoreProvider
import kotlin.reflect.KProperty


fun interface ChangeNotifier<T : IStoreProvider, Data, Source> {
    fun T.notify(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, Source>,
        data: Data?,
        source: Source?
    )
}

internal operator fun <T : IStoreProvider, Data, Source> ChangeNotifier<T, Data, Source>.plus(other: ChangeNotifier<T, Data, Source>) =
    ChangeNotifier<T, Data, Source> { property, process, data, source ->
        notify(property, process, data, source)
        with(other) {
            notify(property, process, data, source)
        }
    }