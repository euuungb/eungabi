package com.blucky8649.decompose_navhost.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner

class NavController(
    componentContext: ComponentContext
) : BackHandlerOwner, ComponentContext by componentContext {

    private var _graph: NavGraph? = null
    var graph: NavGraph
        get() {
        return _graph ?: error("Graph is not set")
    } set(value) {
        _graph = value
    }

    private val navigation = StackNavigation<String>()

    val backStack: Value<ChildStack<*, NavBackStackEntry>> by lazy {
        childStack(
            source = navigation,
            serializer = null,
            initialConfiguration = graph.startDestination,
            handleBackButton = true,
            childFactory = { route, _ ->
                NavBackStackEntry(graph.findDestination(route))
            }
        )
    }

    fun popBackStack(onCompleted: (isSuccess: Boolean) -> Unit = {})
            = navigation.pop(onComplete = onCompleted)

    fun navigate(route: String) = navigation.push(route)
}

@Composable
fun rememberNavController(
    componentContext: ComponentContext
) = remember { NavController(componentContext) }