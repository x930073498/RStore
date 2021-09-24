package com.x930073498.lifecycle.internal

import com.x930073498.lifecycle.core.Removable

interface IStateProvider {
    val state: State
}

internal fun IStateProvider.asRemovable() =object :Removable {
    override fun remove() {
        state.disable()
    }

}
