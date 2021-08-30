package com.x930073498.rstore.property.factory

import com.x930073498.rstore.core.Disposable
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.internal.registerPropertyChangedListenerImpl
import com.x930073498.rstore.internal.valueFromProperty
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.SourceFactory
import kotlin.reflect.KProperty

internal  class TargetPropertyFactory<T : IStoreProvider, Data, Source>(
    private val targetProperty: KProperty<Data>,
    private val transfer: Data.() -> Source
) :
    SourceFactory<T, Source, Source> {

    private var disposable: Disposable? = null

    override fun T.createSource(
        property: KProperty<*>,
        process: DelegateProcess<T, Source, Source>,
        data: Source?
    ): Source {
        disposable?.dispose()
        fun getSource(): Source {
            return transfer.invoke(valueFromProperty(targetProperty) as Data)
        }
        var pre = data ?: getSource()
        disposable = registerPropertyChangedListenerImpl(targetProperty) {
            val value = transfer.invoke(this@registerPropertyChangedListenerImpl)
            with(process.delegate) {
                with(process.equals) {
                    if (!equals(value, pre)) {
                        pre = value
                        setValue(
                            this@createSource,
                            property,
                            value
                        )
                    }
                }

            }
        }
        return pre
    }

    override fun T.transform(
        property: KProperty<*>,
        process: DelegateProcess<T, Source, Source>,
        source: Source
    ): Source? {
        return source
    }


}