package com.x930073498.sample

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.x930073498.rstore.*

class MainViewModel(application: Application, handle: SavedStateHandle) :
    SaveStateStoreViewModel(application, handle) {
//    val countOb by property(MainViewModel::count, isAnchorProperty = true) {
//        "$this"
//    }


    //    var count by property(0, shouldSaveState = true)
//    var count by 0.propertyFeature(Feature.Anchor)
    var count by property(0,isAnchorProperty = true,shouldSaveState = true)//.propertyFeature(Feature.Anchor)
//    var count by 0.asProperty()
//    var count by 0.propertyFeature(Feature, Feature.SaveState)
    val data by flowProperty(count, isAnchorProperty = true, shouldSaveState = true)
    var list by listProperty<String>(isAnchorProperty = true)

}