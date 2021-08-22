package com.x930073498.rstore.property

import com.x930073498.rstore.core.IStoreProvider
import kotlin.reflect.KProperty

 interface SourceFactory<T : IStoreProvider, Data, Source> {

    fun T.createSource(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, Source>,
        data: Data?
    ): Source

    fun T.transform(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, Source>,
        source: Source
    ): Data?

}