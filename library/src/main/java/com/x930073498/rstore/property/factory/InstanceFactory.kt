package com.x930073498.rstore.property.factory

import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.SourceFactory
import kotlin.reflect.KProperty

internal class InstanceFactory<T : IStoreProvider, Source>(private val defaultValue: Source) :
    SourceFactory<T, Source, Source> {
    override fun T.createSource(
        property: KProperty<*>,
        process: DelegateProcess<T, Source, Source>,
        data: Source?
    ): Source {
        return data ?: defaultValue
    }

    override fun T.transform(
        property: KProperty<*>,
        process: DelegateProcess<T, Source, Source>,
        source: Source
    ): Source? {
        return source
    }

}