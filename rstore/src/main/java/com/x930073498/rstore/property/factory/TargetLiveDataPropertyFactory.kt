package com.x930073498.rstore.property.factory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.internal.registerPropertyChangedListenerImpl
import com.x930073498.rstore.property.SourceFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.reflect.KProperty

internal class TargetLiveDataPropertyFactory<T : IStoreProvider, Data, Target>(
    private val provider: T,
    private val targetProperty: KProperty<Data>,
    private val transfer: Data.() -> Target,
) :
    SourceFactory<Target, LiveData<Target>> {
    override fun createSource(data: Target?): LiveData<Target> {
        val liveData = MutableLiveData<Target>()
        provider.registerPropertyChangedListenerImpl(targetProperty) {
            liveData.postValue(transfer(this))
        }
        return liveData
    }
}