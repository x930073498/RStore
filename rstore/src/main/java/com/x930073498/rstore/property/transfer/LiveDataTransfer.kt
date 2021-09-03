package com.x930073498.rstore.property.transfer

import androidx.lifecycle.LiveData
import com.x930073498.rstore.property.SourceTransfer

class LiveDataTransfer<T,R:LiveData<T>>():SourceTransfer<R,T> {
    override fun transform(source: R): T? {
        return source.value
    }
}