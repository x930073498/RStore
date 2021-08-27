package com.x930073498.rstore.core

import kotlin.reflect.KProperty

interface PropertyContainer : Disposable {

    fun getDelegateProperty(property: KProperty<*>): KProperty<*>?

    fun addProperty(property: KProperty<*>)

    fun removeDelegateProperty(property: KProperty<*>)

}