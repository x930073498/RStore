package com.x930073498.rstore.property

import com.x930073498.rstore.Equals
import com.x930073498.rstore.core.IStoreProvider
import kotlin.reflect.KProperty

 data class DelegateProcess<T : IStoreProvider, Data, Source> constructor(
    val factory: SourceFactory<T, Data, Source>,
    val initializer: SourceInitializer<T, Data, Source>,
    val notifier: ChangeNotifier<T, Data, Source>,
    val checker: StateChecker<T, Data, Source>,
    val equals: Equals<Data>
) {

    private fun T.transform(property: KProperty<*>, source: Source?): Data? {
        if (source == null) return null
        return with(factory) {
            transform(property, this@DelegateProcess, source)
        }
    }

    fun T.equals(property: KProperty<*>, first: Source?, second: Source?): Boolean {
        if (first != second) return false
        val firstData = transform(property, first)
        val secondData = transform(property, second)
        return equals.equals(firstData, secondData)
    }
}