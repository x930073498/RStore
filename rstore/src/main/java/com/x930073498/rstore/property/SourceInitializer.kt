package com.x930073498.rstore.property

import com.x930073498.rstore.core.IStoreProvider
import kotlin.reflect.KProperty

interface SourceInitializer<T : IStoreProvider, Data, Source> {

    fun T.onInitialized(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, Source>,
        data: Data?,
        source: Source
    )

}