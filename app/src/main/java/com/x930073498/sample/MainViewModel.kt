package com.x930073498.sample

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.x930073498.rstore.SaveStateStoreViewModel
import com.x930073498.rstore.property

class MainViewModel(application: Application, handle: SavedStateHandle) :
    SaveStateStoreViewModel(application, handle) {
    var count = 0
    var data by property("$count", isAnchorProperty = true)
}