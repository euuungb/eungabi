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
package com.blucky8649.eungabi.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import com.blucky8649.eungabi.utils.withScheme
import com.eygraber.uri.Uri

const val SCHEME = "navhost://"

class EunGabiGraph (
    val startDestination: String,
    private val destinations: Map<String, EunGabiDestination>
) {
    fun findDestination(route: String): EunGabiDestination {
        val uri = Uri.parse(withScheme(route))
        val host = uri.host
        return destinations[host] ?: error("Destination not found")
    }
}

class EunGabiGraphBuilder {
    private val destinations = mutableMapOf<String, EunGabiDestination>()

    private var _startDestination: String? = null
    var startDestination: String get() {
        return _startDestination ?: error("Start destination is not set")
    }
        set(value) { _startDestination = value }

    fun composable(
        route: String,
        content: @Composable AnimatedVisibilityScope.(EunGabiEntry) -> Unit
    ) {
        val fullRoute = withScheme(route)
        val host = Uri.parse(fullRoute).host ?: route
        destinations[host] = EunGabiDestination(fullRoute, content = content)
    }

    internal fun build() = EunGabiGraph(startDestination, destinations)
}