package com.x930073498.lifecycle.core

 interface Removable {
    companion object:Removable{
        override fun remove() {

        }

    }
    fun remove()

}