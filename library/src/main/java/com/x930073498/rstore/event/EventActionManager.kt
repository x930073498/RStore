package com.x930073498.rstore.event

import com.x930073498.rstore.core.Disposable
import com.x930073498.rstore.core.IStoreProvider

internal class EventActionManager<S : IStoreProvider, T>(private val provider: S):Disposable {
    private var isBegan = false
    private val actions = arrayListOf<EventAction<S, T>>()


    fun begin() {
        isBegan = true
    }


    fun pushEventAction(action: EventAction<S, T>) {
        if (!isBegan) return
        actions.add(action)
    }


    suspend fun runAction(data: T) {
        with(provider) {
            actions.forEach {
                with(it) {
                    init(data)
                    if (enable(data)) {
                        process(data)
                    }
                }
            }
        }
        actions.clear()
    }

    fun end() {
        isBegan = false
    }

    override fun dispose() {
        isBegan=false
        actions.clear()
    }


}