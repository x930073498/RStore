package com.x930073498.rstore.property.factory

import com.x930073498.rstore.core.Disposable
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.internal.valueFromProperty
import com.x930073498.rstore.property.SourceFactory
import kotlin.reflect.KProperty

internal class TargetPropertyFactory<T : IStoreProvider, Data, Source>(
    private val provider: T,
    private val targetProperty: KProperty<Data>,
    private val transfer: Data.() -> Source
) :
    SourceFactory<Source, Source> {
    override fun createSource(
        data: Source?
    ): Source {
        fun getSource(): Source {
            return with(provider) {
                transfer.invoke(valueFromProperty(targetProperty) as Data)
            }
        }
        return data ?: getSource()
    }


}