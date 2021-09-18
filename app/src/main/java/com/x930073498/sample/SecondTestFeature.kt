package com.x930073498.sample

import com.x930073498.features.core.IFeature

interface SecondTestFeature:IFeature {
    fun printCreate(){}
    fun printStart(){}
    fun printResume(){}

    fun printPause(){}
    fun printStop(){}
    fun printDestroy(){}
}