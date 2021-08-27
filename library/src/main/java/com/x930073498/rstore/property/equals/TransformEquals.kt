package com.x930073498.rstore.property.equals

import com.x930073498.rstore.core.Equals
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
                with(factory) {
                    val first = if (data1 == null) null else transform(property, process, data1)
                    val second = if (data2 == null) null else transform(property, process, data2)
                    equals.equals(first, second)
                }
            }
        }
    }
}