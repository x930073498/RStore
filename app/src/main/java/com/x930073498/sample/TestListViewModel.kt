package com.x930073498.sample

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.x930073498.rstore.SaveStateStoreViewModel
import com.x930073498.rstore.core.launchOnIO
import com.x930073498.rstore.listProperty
import com.x930073498.rstore.property
import kotlinx.coroutines.delay
import java.util.*

class TestListViewModel(application: Application, handle: SavedStateHandle) :
    SaveStateStoreViewModel(application, handle) {

    var goods by listProperty<String>(isAnchorProperty = true, shouldSaveState = true)

    var isRefresh by property(false, isAnchorProperty = true,shouldSaveState = true)

    fun refresh() {
        launchOnIO {
            isRefresh = true
            delay(1000)
            goods= arrayListOf(UUID.randomUUID().toString())
            isRefresh=false
        }
    }

}
