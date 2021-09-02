package com.x930073498.rstore.property

import java.util.*
import kotlin.reflect.KProperty

data class WrapperData<Data, Source>(
    val property: KProperty<*>,
    val source: Source,
    val data: Data,
    val id: String = UUID.randomUUID().toString()
) {

}