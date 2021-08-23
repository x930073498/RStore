package com.x930073498.rstore.property.factory

import androidx.lifecycle.MutableLiveData
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.SourceFactory
import kotlin.reflect.KProperty

internal class ObservableLiveData<T> : MutableLiveData<T> {
    internal var action: T.() -> Unit

    constructor(t: T, action: T.() -> Unit = {}) : super(t) {
        this.action = action
    }

    constructor(action: T.() -> Unit = {}) : super() {
        this.action = action
    }

    override fun setValue(value: T) {
        super.setValue(value)
        action(value)
    }

}

class MutLiveDataFactory<T : IStoreProvider, Data>(private val defaultValue: Data?) :
    SourceFactory<T, Data, MutableLiveData<Data>> {
    override fun T.createSource(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, MutableLiveData<Data>>,
        data: Data?
    ): MutableLiveData<Data> {
        val liveData = ObservableLiveData<Data>()
        liveData.apply {
            action = {
                with(process.notifier) {
                    notify(property, process, data, this@apply)
                }
            }
            if (data != null) {
                postValue(data)
            }else if (defaultValue!=null){
                postValue(defaultValue)
            }
        }
        return liveData
    }

    override fun T.transform(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, MutableLiveData<Data>>,
        source: MutableLiveData<Data>
    ): Data? {
        return source.value
    }

}