package com.x930073498.sample

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.x930073498.rstore.SaveStateStoreViewModel
import com.x930073498.rstore.listProperty
import com.x930073498.rstore.liveDataProperty
import com.x930073498.rstore.property

class MainViewModel(application: Application, handle: SavedStateHandle) :
    SaveStateStoreViewModel(application, handle) {
    var count = 0
    val data by liveDataProperty(count, isAnchorProperty = true)

}