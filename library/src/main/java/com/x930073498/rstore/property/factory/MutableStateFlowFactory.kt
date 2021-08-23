package com.x930073498.rstore.property.factory

import androidx.lifecycle.MutableLiveData
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.SourceFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty


class MutableStateFlowFactory<T : IStoreProvider, Data>(private val defaultValue: Data) :
    SourceFactory<T, Data, MutableStateFlow<Data>> {
    override fun T.createSource(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, MutableStateFlow<Data>>,
        data: Data?
    ): MutableStateFlow<Data> {
        return MutableStateFlow(data ?: defaultValue).apply {
            coroutineScope.launch(io) {
                collect {
                    with(process.notifier) {
                        notify(property, process, data, this@apply)
                    }
                }
            }
        }
    }

    override fun T.transform(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, MutableStateFlow<Data>>,
        source: MutableStateFlow<Data>
    ): Data? {
        return source.value
    }


}