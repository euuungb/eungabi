package com.blucky8649.decompose_navhost.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.blucky8649.decompose_navhost.utils.withScheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavController(
    componentContext: ComponentContext
) : BackHandlerOwner, ComponentContext by componentContext {

    private var _graph: NavGraph? = null
    var graph: NavGraph get() {
        return _graph ?: error("Graph is not set")
    } set(value) {
        _graph = value
    }

    private val navigation = StackNavigation<NavConfiguration>()

    val backStack: Value<ChildStack<*, NavBackStackEntry>> by lazy {
        childStack(
            source = navigation,
            serializer = null,
            initialConfiguration = NavConfiguration(
                destination = graph.findDestination(graph.startDestination),
            ),
            handleBackButton = true,
            childFactory = { config, _ ->
                NavBackStackEntry(
                    destination = config.destination,
                    navOptions = config.navOptions,
                    arguments = NavArguments(config.fullRoute)
                )
            }
        )
    }

    fun popBackStack(onCompleted: (isSuccess: Boolean) -> Unit = {}) {
        val entry = backStack.value.items.lastOrNull()?.instance ?: return

        val inclusive = entry.navOptions.inclusive
        val popUpToRoute = entry.navOptions.popUpToRoute

        if (popUpToRoute.isEmpty()) {
            navigation.pop(onCompleted)
            return
        }

        navigation.popWhile { (topDestinationOfStack, _) ->
            topDestinationOfStack.fullRoute != popUpToRoute
        }

        if (inclusive) { navigation.pop(onComplete = onCompleted) }
    }

    fun navigate(
        route: String,
        navOptionsBuilder: NavOptionsBuilder.() -> Unit = {}
    ) {
        val navOptions = NavOptionsBuilder()
            .apply(navOptionsBuilder)
            .build()

        val newConfig = NavConfiguration(
            destination = graph.findDestination(route),
            navOptions = navOptions,
            fullRoute = withScheme(route)
        )

        navigation.push(newConfig)
    }
}



data class NavConfiguration(
    val destination: Destination,
    val navOptions: NavOptions = NavOptions(),
    val fullRoute: String = destination.route
)

@Composable
fun rememberNavController(
    componentContext: ComponentContext
) = remember { NavController(componentContext) }