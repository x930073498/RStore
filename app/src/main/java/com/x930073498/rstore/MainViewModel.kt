package com.x930073498.rstore

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.x930073498.rstore.core.launchOnIO

class MainViewModel(application: Application, handle: SavedStateHandle) :
    SaveStateStoreViewModel(application, handle) {

     var count by property(0,isAnchorProperty = true)
    var data by property("$count",isAnchorProperty = true)


    fun changeProperty() {
        data = "${++count}"
    }
}