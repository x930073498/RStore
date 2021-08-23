package com.x930073498.rstore.property.equals

import com.x930073498.rstore.Equals
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.DelegateProcess
import kotlin.reflect.KProperty

class TransformEquals<T : IStoreProvider, Data, Source>(
    private val provider: T,
    private val property: KProperty<*>,
    private val process: DelegateProcess<T, Data, Source>
) : Equals<Source> {
    override fun equals(data1: Source?, data2: Source?): Boolean {
        if (data1 != data2) return false
        return with(provider) {
            with(process) {
                equals(property, data1, data2)
            }
        }
    }
}