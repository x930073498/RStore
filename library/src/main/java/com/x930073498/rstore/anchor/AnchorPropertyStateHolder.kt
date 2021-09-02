package com.x930073498.rstore.anchor

import com.x930073498.rstore.core.Disposable
import com.x930073498.rstore.util.LockProvider
import com.x930073498.rstore.util.doLock
import java.util.*
import java.util.concurrent.locks.ReentrantLock

class AnchorPropertyStateHolder : LockProvider ,Disposable{
    override val lock = ReentrantLock()
    private val map = mutableMapOf<String, Boolean>()


    fun setPropertyState(name: String, isPropertyChanged: Boolean) {
        doLock {
            map[name] = isPropertyChanged
        }
    }

    fun isPropertyChanged(name: String): Boolean {
        return doLock {
            map[name] != false
        }
    }

    override fun dispose() {
        doLock {
            map.clear()
        }
    }

}