package com.blucky8649.decompose_navhost.navigation.experimental

import com.blucky8649.decompose_navhost.navigation.NavBackStackEntry

internal object EunGabiState {
    private var backQueue = ArrayDeque<NavBackStackEntry>()

    fun save(backQueue: ArrayDeque<NavBackStackEntry>) {
        this.backQueue.addAll(backQueue)
    }

    fun restore(): ArrayDeque<NavBackStackEntry> {
        val result = ArrayDeque(backQueue.toList())
        return result.also { backQueue.clear() }
    }
}