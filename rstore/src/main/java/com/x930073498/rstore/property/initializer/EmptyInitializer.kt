package com.x930073498.rstore.property.initializer

import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.SourceInitializer
import kotlin.reflect.KProperty

internal class EmptyInitializer<T : IStoreProvider, Data, Source> :
    SourceInitializer<T, Data, Source> {
    override fun T.onInitialized(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, Source>,
        data: Data?,
        source: Source
    ) {

    }


}