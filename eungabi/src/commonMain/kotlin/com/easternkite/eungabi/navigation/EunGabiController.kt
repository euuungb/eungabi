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

class EunGabiController {

    private var _graph: EunGabiGraph? = null
    var graph: EunGabiGraph
        get() {
        return _graph ?: error("Graph is not set")
    } set(value) {
        if (backQueue.isEmpty()) {
            val initialEntity = createEntry(
                index = 0,
                route = value.startDestination,
                graph = value
            )
            backQueue = ArrayDeque(listOf(initialEntity))
        }
        _backStack.update { backQueue.toList() }
        _graph = value
    }

    internal val isPop = mutableStateOf(false)

    private var backQueue = ArrayDeque<EunGabiEntry>()

    private val _backStack = MutableStateFlow<List<EunGabiEntry>>(listOf())
    val backStack: StateFlow<List<EunGabiEntry>>  = _backStack.asStateFlow()

    fun navigateUp(): Boolean {
        if (backQueue.size <= 1) return false
        var currentEntity = backQueue.lastOrNull() ?: return false
        var removedEntry: EunGabiEntry?
        val targetRoute = findPreviousEntity(currentEntity).eunGabiDestination.route

        do {
            removedEntry = backQueue.removeLastOrNull()
            backQueue.lastOrNull()?.also { currentEntity = it }
        } while (currentEntity.eunGabiDestination.route != targetRoute)

        _backStack.update { backQueue.toList() }
        isPop.value = true
        return removedEntry != null
    }

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

    internal fun findPreviousEntity(entity: EunGabiEntry): EunGabiEntry {
        val targetRoute = entity.navOptions.popUpToRoute
        val inclusive = entity.navOptions.inclusive

        val index = backQueue
            .map { it.eunGabiDestination.route }
            .indexOf(targetRoute)

        if (index == -1) {
            return backQueue[backQueue.lastIndex -1]
        }

        val targetIndex = if (inclusive) {
            index.minus(1).coerceAtLeast(0)
        } else {
            index
        }

        return backQueue[targetIndex]
    }

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
            index = index
        )
    }

    fun saveState(): Boolean {
        EunGabiState.save(backQueue)
        return true
    }

    fun restoreState() {
        val result = EunGabiState.restore()
        backQueue = result
    }
}

@Composable
fun rememberEunGabiController(): EunGabiController
    = rememberSaveable(saver = eunGabiControllerSaver()) {
            EunGabiController()
    }

private fun eunGabiControllerSaver() = Saver<EunGabiController, Boolean>(
    save = { it.saveState() },
    restore = { EunGabiController().apply(EunGabiController::restoreState) }
)