package com.x930073498.rstore.property.factory

import com.x930073498.rstore.property.SourceFactory
import kotlinx.coroutines.flow.MutableStateFlow


internal class MutableStateFlowFactory<Data>(private val defaultValue: Data) :
    SourceFactory< Data, MutableStateFlow<Data>> {
    override fun createSource(
        data: Data?
    ): MutableStateFlow<Data> {
        return MutableStateFlow(data?:defaultValue)
    }

}