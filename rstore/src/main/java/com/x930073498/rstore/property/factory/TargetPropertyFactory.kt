package com.x930073498.rstore.property.factory

import com.x930073498.rstore.core.Disposable
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.internal.valueFromProperty
import com.x930073498.rstore.property.SourceFactory
import com.x930073498.rstore.property.SourceTransfer
import kotlin.reflect.KProperty

internal class TargetPropertyFactory<T : IStoreProvider, Data, Target, Source>(
    private val provider: T,
    private val targetProperty: KProperty<Data>,
    private val transfer: Data.() -> Target,
    private val sourceTransfer: Target.() -> Source
) :
    SourceFactory<Target, Source> {
    override fun createSource(
        data: Target?
    ): Source {
        fun getSource(): Source {
            return with(provider) {
                sourceTransfer(transfer.invoke(valueFromProperty(targetProperty)))
            }
        }
        return getSource()
    }


}