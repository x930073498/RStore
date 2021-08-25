package com.x930073498.rstore.core

import android.os.Parcelable
import com.x930073498.rstore.Disposable
import com.x930073498.rstore.MapStore
import kotlinx.coroutines.CoroutineScope
import java.io.Closeable
import kotlin.coroutines.CoroutineContext


interface Coroutine {
    val coroutineScope: CoroutineScope
    val io: CoroutineContext
    val main: CoroutineContext
}

interface IStoreProvider : Coroutine {
    val store: IStore
}

interface ISaveStateStoreProvider {
    val saveStateStore: ISaveStateStore
}


interface ISaveStateStore :Disposable{
    val id:String
    fun remove(key: String)

    fun put(key: String, value: Any?)

    fun get(key: String): Any?

    fun clear()
    override fun dispose() {
        clear()
    }
}
interface IStore : Disposable {
    fun remove(key: Any)

    fun put(key: Any, value: Any?)

    fun get(key: Any): Any?

    fun clear()
    override fun dispose() {
        clear()
    }


}

