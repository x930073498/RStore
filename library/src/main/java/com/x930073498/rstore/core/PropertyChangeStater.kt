package com.x930073498.rstore.core

fun interface PropertyChangeStater  {

    companion object:PropertyChangeStater{
        override fun start(handler: ScopeHandler) {
            handler.resume()
        }

    }
    fun start(handler:ScopeHandler)

}

