package com.x930073498.lifecycle.internal

class State internal constructor(
    private var state: Boolean = true,
    private val stateChange: Boolean.() -> Unit = {}
) {

    fun disable() {
        if (state) {
            state = false
            stateChange(false)
        }
    }

    fun enable() {
        if (!state) {
            state = true
            stateChange(true)
        }
    }

    operator fun invoke(): Boolean {
        return state
    }
}