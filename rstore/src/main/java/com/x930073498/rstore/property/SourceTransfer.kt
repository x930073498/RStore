package com.x930073498.rstore.property

fun interface SourceTransfer<Source, Data> {

    fun transform(source: Source): Data?
}