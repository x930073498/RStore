package com.x930073498.rstore.property.factory

import androidx.lifecycle.MutableLiveData
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.SourceFactory
import kotlin.reflect.KProperty


internal class MutLiveDataFactory<Data>(private val defaultValue: Data?) :
    SourceFactory<Data, MutableLiveData<Data>> {
    override fun createSource(
        data: Data?
    ): MutableLiveData<Data> {
        val temp=data?:defaultValue
        return if (temp != null) MutableLiveData<Data>(temp) else MutableLiveData()
    }


}