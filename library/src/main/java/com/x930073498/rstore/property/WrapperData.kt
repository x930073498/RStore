package com.x930073498.rstore.property

import kotlin.reflect.KProperty

data class WrapperData<Data, Source>(
    val property: KProperty<*>,
    val source: Source,
    val data: Data,

) {

}