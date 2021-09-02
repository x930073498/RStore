package com.x930073498.rstore.property.transfer

import com.x930073498.rstore.property.SourceTransfer

class InstanceTransfer<Source>:SourceTransfer<Source,Source> {
    override fun transform(source: Source): Source? {
        return source
    }
}