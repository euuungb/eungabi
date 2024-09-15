package com.blucky8649.decompose_navhost.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable

class NavGraph (
    val startDestination: String,
    private val destinations: Map<String, Destination>
) {
    fun findDestination(route: String) : Destination = destinations[route]
        ?: error("Destination not found")
}

class NavGraphBuilder {
    private val destinations = mutableMapOf<String, Destination>()

    private var _startDestination: String? = null
    var startDestination: String get() {
        return _startDestination ?: error("Start destination is not set")
    }
        set(value) { _startDestination = value }

    fun composable(
        route: String,
        content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
    ) {
        destinations[route] = Destination(route, content = content)
    }

    internal fun build() = NavGraph(startDestination, destinations)
}