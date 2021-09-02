package com.x930073498.rstore.core

import com.x930073498.rstore.util.AwaitState

fun interface PropertyChangeStater  {

    companion object:PropertyChangeStater{
        override fun start(startHandle: AwaitState<Boolean>) {
            startHandle.setState(true)
        }

    }
    fun start(startHandle:AwaitState<Boolean>)

}

