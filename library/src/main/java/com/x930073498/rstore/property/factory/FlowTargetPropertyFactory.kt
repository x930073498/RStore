package com.x930073498.rstore.property.factory

import com.x930073498.rstore.core.Disposable
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.internal.*
import com.x930073498.rstore.internal.registerPropertyChangedListenerImpl
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.SourceFactory
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlin.reflect.KProperty

internal class FlowTargetPropertyFactory<T : IStoreProvider, Data, Source>(
    private val targetProperty: KProperty<Data>,
    private val transfer: suspend Data.() -> Source
) :
    SourceFactory<T, Source, SharedFlow<Source>> {

    private var disposable: Disposable? = null
    override fun T.createSource(
        property: KProperty<*>,
        process: DelegateProcess<T, Source, SharedFlow<Source>>,
        data: Source?
    ): SharedFlow<Source> {
        disposable?.dispose()
        val channel = Channel<Data>(1, BufferOverflow.DROP_OLDEST)
        val flow = flow {
            var pre = data
            if (data != null) {
                emit(data)
            }
            for (value in channel) {
                val current = transfer.invoke(value)
                with(process.equals) {
                    if (!equals(current, pre)) {
                        pre = current
                        emit(current)
                    }
                }
            }
        }
        disposable = registerPropertyChangedListenerImpl(targetProperty) {
            channel.trySend(this)
        } + Disposable {
            channel.close()
        }
        return flow.shareIn(coroutineScope, started = SharingStarted.Lazily, 1)
    }

    override fun T.transform(
        property: KProperty<*>,
        process: DelegateProcess<T, Source, SharedFlow<Source>>,
        source: SharedFlow<Source>
    ): Source? {
        return source.replayCache.firstOrNull()
    }


}