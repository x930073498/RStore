package com.x930073498.rstore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.x930073498.rstore.core.ISaveStateStore
import com.x930073498.rstore.core.IStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class AbstractStoreViewModel(application: Application) : AndroidViewModel(application),
    IStoreProviderComponent {
    override val store: IStore = MapStore()
    override val coroutineScope: CoroutineScope = viewModelScope
    override val io: CoroutineContext = Dispatchers.IO
    override val main: CoroutineContext = Dispatchers.Main
    override fun onCleared() {
        super.onCleared()
        store.clear()
    }
}