package com.x930073498.rstore.property.initializer

import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.core.launchOnIO
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.SourceInitializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

class FlowInitializer<T : IStoreProvider, Data,F:Flow<Data>> :
    SourceInitializer<T, Data,F> {
    override fun T.onInitialized(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, F>,
        data: Data?,
        source: F
    ) {
        launchOnIO {
            var pre = data
            val dataEquals = process.equals.dataEquals
            source.collect {
                if (!dataEquals.equals(it, pre)) {
                    with(process.notifier) {
                        notify(property, process, it, source)
                    }
                    pre = it
                }
            }
        }
    }
}