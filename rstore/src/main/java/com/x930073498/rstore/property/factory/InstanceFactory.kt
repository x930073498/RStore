package com.x930073498.rstore.property.factory

import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.SourceFactory
import kotlin.reflect.KProperty

internal class InstanceFactory<Source>(private val defaultValue: Source) :
    SourceFactory<Source, Source> {
    override fun createSource(
        data: Source?
    ): Source {
        return data ?: defaultValue
    }
}