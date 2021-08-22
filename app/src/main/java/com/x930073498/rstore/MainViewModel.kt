package com.x930073498.rstore

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.x930073498.rstore.core.launchOnIO

class MainViewModel(application: Application, handle: SavedStateHandle) :
    SaveStateStoreViewModel(application, handle) {

    private var count = 0
    var data by property("",isAnchorProperty = true)


    fun changeProperty() {
        data = "${++count}"
    }
}