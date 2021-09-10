package com.x930073498.rstore.property.initializer

import androidx.lifecycle.LiveData
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.core.launchOnMain
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.SourceInitializer
import kotlin.reflect.KProperty


internal class LiveDataInitializer<T : IStoreProvider, Data, O : LiveData<Data>>() :
    SourceInitializer<T, Data, O> {
    override fun T.onInitialized(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, O>,
        data: Data?,
        source: O
    ) {
        var pre = data
        val equals = process.equals
        launchOnMain {
            source.observeForever {
                if (!equals.dataEquals.equals(it, pre)) {
                    pre = it
                    with(process.notifier) {
                        notify(property, process, pre, source)
                    }
                }
            }
        }


    }

}