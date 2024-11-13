/*
 * Copyright 2024 easternkite
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easternkite.eungabi.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import com.easternkite.eungabi.utils.withScheme
import com.eygraber.uri.Uri

/**
 * A navigation scheme, which is used to convert and parse a route to a destination.
 */
const val SCHEME = "navhost://"

/**
 * The navigation graph.
 * It holds destinations to navigate and find them by route.
 */
class EunGabiGraph(
    /**
     * The start destination. It is added to the backStack of the [EunGabiController] initially.
     */
    val startDestination: String,
    /**
     * The destinations of the graph. It holds all the destinations of the graph.
     * It is used to find a destination by route.
     */
    private val destinations: Map<String, EunGabiDestination>
) {
    /**
     * Finds a destination by route.
     * @param route The route of the destination.
     * @return The [EunGabiDestination] with the given route.
     * @throws IllegalStateException If the destination is not found.
     */
    fun findDestination(route: String): EunGabiDestination {
        val uri = Uri.parse(withScheme(route))
        val host = uri.host
        return destinations[host] ?: error("Destination not found")
    }
}

/**
 * A builder class for the [EunGabiGraph].
 * destinations can be added to the graph using the [composable] function.
 */
class EunGabiGraphBuilder {
    /**
     * The destinations of the graph. It holds all the destinations of the graph.
     * It is used to find a destination by route.
     */
    private val destinations = mutableMapOf<String, EunGabiDestination>()

    /**
     * The backing property of [startDestination].
     */
    private var _startDestination: String? = null

    /**
     * Sets the start destination of the graph.
     */
    var startDestination: String get() {
        return _startDestination ?: error("Start destination is not set")
    }
        set(value) {
            _startDestination = value
        }

    /**
     * It adds a destination to the graph.
     */
    fun composable(
        route: String,
        content: @Composable AnimatedVisibilityScope.(EunGabiEntry) -> Unit
    ) {
        val fullRoute = withScheme(route)
        val host = Uri.parse(fullRoute).host ?: route
        destinations[host] = EunGabiDestination(fullRoute, content = content)
    }

    /**
     * Builds the [EunGabiGraph].
     */
    internal fun build() = EunGabiGraph(startDestination, destinations)
}
