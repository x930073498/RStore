package com.x930073498.features.core

fun interface Removable {
    companion object:Removable{
        override fun remove() {

        }
    }
    fun remove()
}