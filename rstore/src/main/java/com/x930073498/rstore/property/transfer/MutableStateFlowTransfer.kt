package com.x930073498.rstore.property.transfer

import com.x930073498.rstore.property.SourceTransfer
import kotlinx.coroutines.flow.MutableStateFlow

class MutableStateFlowTransfer<T> : SourceTransfer<MutableStateFlow<T>, T> {
    override fun transform(source: MutableStateFlow<T>): T? {
        return source.value
    }
}