package com.blucky8649.decompose_navhost.navigation.experimental

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.blucky8649.decompose_navhost.navigation.NavArguments
import com.blucky8649.decompose_navhost.navigation.NavBackStackEntry
import com.blucky8649.decompose_navhost.navigation.NavGraph
import com.blucky8649.decompose_navhost.navigation.NavOptions
import com.blucky8649.decompose_navhost.navigation.NavOptionsBuilder
import com.blucky8649.decompose_navhost.utils.withScheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EunGabiController {

    private var _graph: NavGraph? = null
    var graph: NavGraph
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

    private var backQueue = ArrayDeque<NavBackStackEntry>()

    private val _backStack = MutableStateFlow<List<NavBackStackEntry>>(listOf())
    val backStack: StateFlow<List<NavBackStackEntry>>  = _backStack.asStateFlow()

    fun navigateUp(): Boolean {
        val removedEntity = backQueue.removeLastOrNull()
        _backStack.update { backQueue.toList() }
        return removedEntity != null
    }

    fun navigate(
        route: String,
        navOptionsBuilder: NavOptionsBuilder.() -> Unit = {}
    ) {
        val navOptions = NavOptionsBuilder().apply(navOptionsBuilder).build()
        val newEntry = createEntry(backQueue.size, route, navOptions)
        backQueue.addLast(newEntry)
        _backStack.update { backQueue.toList() }
    }

    private fun createEntry(
        index: Int,
        route: String,
        navOptions: NavOptions = NavOptions(),
        graph: NavGraph = this.graph,
    ): NavBackStackEntry {
        val fullRoute = withScheme(route)
        val navArguments = NavArguments(fullRoute)
        val destination = graph.findDestination(route)
        return NavBackStackEntry(
            destination = destination,
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