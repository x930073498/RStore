package com.x930073498.rstore.property

interface SourceFactory<Data, Source> {
    fun createSource(
        data: Data?
    ): Source


}