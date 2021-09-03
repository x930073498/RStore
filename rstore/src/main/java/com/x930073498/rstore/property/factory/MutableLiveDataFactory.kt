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

internal class MutLiveDataFactory<Data>(private val defaultValue: Data?) :
    SourceFactory<Data, ObservableLiveData<Data>> {
    override fun createSource(
        data: Data?
    ): ObservableLiveData<Data> {
        val temp=data?:defaultValue
        return if (temp != null) ObservableLiveData<Data>(temp) else ObservableLiveData()
    }


}