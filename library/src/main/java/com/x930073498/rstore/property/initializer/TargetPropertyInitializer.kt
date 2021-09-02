package com.x930073498.rstore.property.initializer

import com.x930073498.rstore.core.Disposable
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.internal.registerPropertyChangedListenerImpl
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.SourceInitializer
import kotlin.reflect.KProperty

class TargetPropertyInitializer<T : IStoreProvider, Data, Source>(
    private val targetProperty: KProperty<Data>,
    private val transfer: Data.() -> Source
) :
    SourceInitializer<T, Source, Source> {
    var disposable: Disposable? = null
    override fun T.onInitialized(
        property: KProperty<*>,
        process: DelegateProcess<T, Source, Source>,
        data: Source?,
        source: Source
    ) {
        disposable?.dispose()
        var pre = source
        disposable = registerPropertyChangedListenerImpl(targetProperty) {
            val value = transfer.invoke(this@registerPropertyChangedListenerImpl)
            with(process.delegate) {
                with(process.equals) {
                    if (!equals(value, pre)) {
                        pre = value
                        setValue(
                            this@onInitialized,
                            property,
                            value
                        )
                    }
                }

            }
        }
    }
}