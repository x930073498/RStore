package com.x930073498.lifecycle.core

fun interface Initializer {
    fun InitializerScope.setup()
}