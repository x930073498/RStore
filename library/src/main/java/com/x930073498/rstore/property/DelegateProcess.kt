package com.x930073498.rstore.property

import com.x930073498.rstore.Equals
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.equals.TransformEquals
import kotlin.reflect.KProperty

data class DelegateProcess<T : IStoreProvider, Data, Source> constructor(
    val factory: SourceFactory<T, Data, Source>,
    val initializer: SourceInitializer<T, Data, Source>,
    val notifier: ChangeNotifier<T, Data, Source>,
    val checker: StateChecker<T, Data, Source>,
    val equals: Equals<Data>
) {
    internal fun realEquals(provider: T, property: KProperty<*>): Equals<Source> {
        return TransformEquals(provider, property, this)
    }


    fun T.equals(property: KProperty<*>, first: Source?, second: Source?): Boolean {
        return realEquals(this, property).equals(first, second)
    }


    fun equals(data1: Data?, data2: Data?): Boolean {
        return equals.equals(data1, data2)
    }
}