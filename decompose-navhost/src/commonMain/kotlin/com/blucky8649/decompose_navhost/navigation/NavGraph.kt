package com.blucky8649.decompose_navhost.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import com.blucky8649.decompose_navhost.utils.withScheme
import com.eygraber.uri.Uri
import io.github.aakira.napier.Napier

const val SCHEME = "navhost://"

class NavGraph (
    val startDestination: String,
    private val destinations: Map<String, Destination>
) {
    fun findDestination(route: String): Destination {
        val uri = Uri.parse(withScheme(route))
        val host = uri.host
        return destinations[host] ?: error("Destination not found")
    }
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
        val fullRoute = withScheme(route)
        val host = Uri.parse(fullRoute).host ?: route
        destinations[host] = Destination(fullRoute, content = content)
    }

    internal fun build() = NavGraph(startDestination, destinations)
}