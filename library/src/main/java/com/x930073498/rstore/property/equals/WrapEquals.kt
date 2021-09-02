package com.x930073498.rstore.property.equals

import com.x930073498.rstore.core.Equals
import com.x930073498.rstore.property.SourceTransfer

class WrapEquals<Source, Data>(
    private val transfer: SourceTransfer<Source, Data>,
    val dataEquals: Equals<Data>
) :
    Equals<Source> {
    companion object {
        fun <Source, Data> WrapEquals<Source, Data>.equals(source: Source?, data: Data?): Boolean {
            val data1 = source?.let { transfer.transform(it) }
            return dataEquals.equals(data1, data)
        }

    }

    override fun equals(data1: Source?, data2: Source?): Boolean {
        if (data1 != data2) return false
        val transformData1 = data1?.let { transfer.transform(it) }
        val transformData2 = data2?.let { transfer.transform(it) }
        return dataEquals.equals(transformData1, transformData2)
    }

}
