package com.x930073498.rstore.property.transfer

import com.x930073498.rstore.property.SourceTransfer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class StateFlowTransfer<T,F:StateFlow<T>> : SourceTransfer<F, T> {
    override fun transform(source: F): T? {
        return source.value
    }
}