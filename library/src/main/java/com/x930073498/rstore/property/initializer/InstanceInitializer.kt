package com.x930073498.rstore.property.initializer

import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.SourceInitializer
import kotlin.reflect.KProperty

internal class InstanceInitializer<T : IStoreProvider, Source> : SourceInitializer<T, Source, Source> {
    override fun T.onInitialized(
        property: KProperty<*>,
        process: DelegateProcess<T, Source, Source>,
        data: Source?,
        source: Source
    ) {

    }


}