package com.x930073498.rstore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.viewModelScope
import com.x930073498.rstore.core.ISaveStateStore
import com.x930073498.rstore.core.IStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class SaveStateStoreViewModel(application: Application, handle: SavedStateHandle) :
    AbstractStoreViewModel(application) {
    override val saveStateStore: ISaveStateStore = SaveStateHandleStore(handle)
}