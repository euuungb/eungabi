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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.easternkite.eungabi.utils.withScheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * A Controller for EunGabi Navigation.
 */
class EunGabiController {
    /**
     * The current navigation graph, that holds destinations registered with [EunGabiGraphBuilder].
     */
    private var _graph: EunGabiGraph? = null
    var graph: EunGabiGraph
        get() {
            return _graph ?: error("Graph is not set")
        }
        set(value) {
            if (backQueue.isEmpty()) {
                val initialEntity =
                    createEntry(
                        index = 0,
                        route = value.startDestination,
                        graph = value,
                    )
                backQueue = ArrayDeque(listOf(initialEntity))
            }
            _backStack.update { backQueue.toList() }
            _graph = value
        }

    /**
     * Whether the current navigation is a pop operation.
     */
    internal val isPop = mutableStateOf(false)

    /**
     * The back queue of the navigation.
     * it holds the history of the navigation entries as Stack data structure.
     */
    private var backQueue = ArrayDeque<EunGabiEntry>()

    /**
     * The back stack of the navigation.
     * It holds the history of the navigation entries emitted by [backQueue].
     */
    private val _backStack = MutableStateFlow<List<EunGabiEntry>>(listOf())
    val backStack: StateFlow<List<EunGabiEntry>> = _backStack.asStateFlow()

    /**
     * Navigates up in the back stack.
     * It uses [findPreviousEntry] to find the previous entity in the back stack.
     * then removes all the entries after the previous entity.
     */
    fun navigateUp(): Boolean {
        if (backQueue.size <= 1) return false
        var currentEntry = backQueue.last()
        val targetRoute = findPreviousEntry(currentEntry)

        do {
            backQueue.removeLast()
            currentEntry = backQueue.last()
        } while (currentEntry.id != targetRoute.id)

        _backStack.update { backQueue.toList() }
        isPop.value = true
        return backQueue.last() == targetRoute
    }

    /**
     * Navigates to the given route.
     *
     * @param route The route to navigate to.
     * @param navOptionsBuilder A builder function to configure the navigation options.
     */
    fun navigate(
        route: String,
        navOptionsBuilder: NavOptionsBuilder.() -> Unit = {}
    ) {
        val navOptions = NavOptionsBuilder().apply(navOptionsBuilder).build()
        val newEntry = createEntry(backQueue.size, route, navOptions)
        backQueue.addLast(newEntry)
        _backStack.update { backQueue.toList() }
        isPop.value = false
    }

    /**
     * Finds the previous entity in the back stack via [NavOptions] of the given entity.
     *
     * @param entry The entity to find the previous entity for.
     * @return The previous [EunGabiEntry] of the given [EunGabiEntry] in the back stack.
     */
    internal fun findPreviousEntry(entry: EunGabiEntry): EunGabiEntry {
        val targetRoute = entry.navOptions.popUpToRoute
        val inclusive = entry.navOptions.inclusive

        val index =
            backQueue
                .map { it.eunGabiDestination.route }
                .indexOf(targetRoute)

        if (index == -1) {
            return backQueue[backQueue.lastIndex - 1]
        }

        val targetIndex =
            if (inclusive) {
                index.minus(1).coerceAtLeast(0)
            } else {
                index
            }

        return backQueue[targetIndex]
    }

    /**
     * Creates a new navigation entry with the given parameters.
     *
     * @param index The index of the navigation entry in the [backQueue].
     * @param route The route of the navigation entry.
     * @param navOptions The navigation options for the navigation entry.
     * @param graph The current navigation graph being used.
     */
    private fun createEntry(
        index: Int,
        route: String,
        navOptions: NavOptions = NavOptions(),
        graph: EunGabiGraph = this.graph,
    ): EunGabiEntry {
        val fullRoute = withScheme(route)
        val navArguments = NavArguments(fullRoute)
        val destination = graph.findDestination(route)
        return EunGabiEntry(
            eunGabiDestination = destination,
            arguments = navArguments,
            navOptions = navOptions,
            index = index,
        )
    }

    /**
     * Saves the current state of the navigation.
     * It saves the [backQueue] of the navigation.
     */
    fun saveState(): Boolean {
        EunGabiState.save(backQueue)
        return true
    }

    /**
     * Restores the state of the navigation.
     * It restores the [backQueue] of the navigation.
     */
    fun restoreState() {
        val result = EunGabiState.restore()
        backQueue = result
        _backStack.update { backQueue.toList() }
    }
}

/**
 * remember an [EunGabiController] instance with [rememberSaveable].
 * It saves and restores the state of the navigation when the configuration changes.
 */
@Composable
fun rememberEunGabiController(): EunGabiController =
    rememberSaveable(saver = eunGabiControllerSaver()) {
        EunGabiController()
    }

/**
 * A [Saver] for [EunGabiController].
 * It provides a custom saver for the navigation controller which saves and restores the [EunGabiController.backQueue] of the navigation.
 */
private fun eunGabiControllerSaver() =
    Saver<EunGabiController, Boolean>(
        save = { it.saveState() },
        restore = { EunGabiController().apply(EunGabiController::restoreState) },
    )
