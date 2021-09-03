package com.x930073498.rstore.property.initializer

import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.SourceInitializer
import com.x930073498.rstore.property.factory.ObservableLiveData
import kotlin.reflect.KProperty


internal class ObservableLiveDataInitializer<T : IStoreProvider, Data>() :
    SourceInitializer<T, Data, ObservableLiveData<Data>> {
    override fun T.onInitialized(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, ObservableLiveData<Data>>,
        data: Data?,
        source: ObservableLiveData<Data>
    ) {
        var pre = data
        val equals = process.equals
        source.action = {
            if (!equals.dataEquals.equals(this, pre)) {
                pre = this
                with(process.notifier) {
                    notify(property, process, pre, source)
                }
            }
        }

    }

}