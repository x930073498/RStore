package com.x930073498.rstore.property

import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.equals.WrapEquals
import kotlin.properties.ReadWriteProperty

data class DelegateProcess<T : IStoreProvider, Data, Source> constructor(
    val factory: SourceFactory<Data, Source>,
    val transfer: SourceTransfer<Source,Data>,
    val initializer: SourceInitializer<T, Data, Source>,
    val notifier: ChangeNotifier<T, Data, Source>,
    val featureProvider: FeatureProvider,
    val delegate:ReadWriteProperty<T,Source>,
    val equals: WrapEquals<Source,Data>
)