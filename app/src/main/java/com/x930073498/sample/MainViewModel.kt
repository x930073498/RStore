package com.x930073498.sample

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.x930073498.rstore.SaveStateStoreViewModel
import com.x930073498.rstore.core.getValue
import com.x930073498.rstore.core.setValue

import com.x930073498.rstore.flowProperty
import com.x930073498.rstore.listProperty
import com.x930073498.rstore.property

class MainViewModel(application: Application, handle: SavedStateHandle) :
    SaveStateStoreViewModel(application, handle) {
    val countOb by property(MainViewModel::count, isAnchorProperty = true) {
        "$this"
    }
    var s: String by ""
    var a by 0
    var count by property(0, shouldSaveState = true)
    val data by flowProperty(count, isAnchorProperty = true, shouldSaveState = true)

    var list by listProperty<String>(isAnchorProperty = true)
}