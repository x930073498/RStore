package com.x930073498.rstore.property.notifier

import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.internal.notifyAnchorPropertyChanged
import com.x930073498.rstore.internal.notifyPropertyChanged
import com.x930073498.rstore.property.*
import kotlin.reflect.KProperty

internal class StandardNotifier<T : IStoreProvider, Data, Source> :
    ChangeNotifier<T, Data, Source> {
    override fun T.notify(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, Source>,
        data: Data?,
        source: Source?
    ) {
        notifyPropertyChanged(property)
        with(process) {
            with(checker) {
                if (isAnchorProperty(property, process)) {
                    notifyAnchorPropertyChanged(property)
                }
            }
        }
    }
}