package com.blucky8649.decompose_navhost.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.StackAnimation as ExperimentalStackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation as DefaultStackAnimation
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack

@ExperimentalDecomposeApi
@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavController,
    startDestination: String,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHostInternal(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        builder = builder,
    )
}

@ExperimentalDecomposeApi
@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavController,
    startDestination: String,
    onNavGraphCreated: (backStack: ChildStack<*, NavBackStackEntry>) -> Unit,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHostInternal(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        onNavGraphCreated = onNavGraphCreated,
        builder = builder,
    )
}

@ExperimentalDecomposeApi
@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavController,
    startDestination: String,
    animation: ExperimentalStackAnimation<Any, Any>,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHostInternal(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        animationExperimental = animation,
        builder = builder,
    )
}

@ExperimentalDecomposeApi
@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavController,
    startDestination: String,
    animation: ExperimentalStackAnimation<Any, Any>,
    onNavGraphCreated: (backStack: ChildStack<*, NavBackStackEntry>) -> Unit,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHostInternal(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        animationExperimental = animation,
        onNavGraphCreated = onNavGraphCreated,
        builder = builder,
    )
}

@ExperimentalDecomposeApi
@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavController,
    startDestination: String,
    animation: DefaultStackAnimation<Any, Any>,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHostInternal(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        animationDefault = animation,
        builder = builder,
    )
}

@ExperimentalDecomposeApi
@Composable
fun NavHost(
    modifier: Modifier = Modifier,
    navController: NavController,
    startDestination: String,
    animation: DefaultStackAnimation<Any, Any>,
    onNavGraphCreated: (backStack: ChildStack<*, NavBackStackEntry>) -> Unit,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHostInternal(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        animationDefault = animation,
        onNavGraphCreated = onNavGraphCreated,
        builder = builder,
    )
}


@ExperimentalDecomposeApi
@Composable
private fun NavHostInternal(
    modifier: Modifier = Modifier,
    navController: NavController,
    startDestination: String,
    animationDefault: DefaultStackAnimation<Any, Any>? = null,
    animationExperimental: ExperimentalStackAnimation<Any, Any>? = null,
    onNavGraphCreated: (backStack: ChildStack<*, NavBackStackEntry>) -> Unit = {},
    builder: NavGraphBuilder.() -> Unit,
) {
    val graph = remember(
        modifier,
        navController,
        startDestination,
        animationDefault,
        animationExperimental,
    ) {
        NavGraphBuilder()
            .apply(builder)
            .also { bd -> bd.startDestination = startDestination }
            .build()
    }

    navController.graph = graph

    val backStack by navController.backStack.subscribeAsState()
    onNavGraphCreated(backStack)

    if (animationDefault != null) {
        AnimatedContent(
            targetState = backStack,
            contentKey = { backStack }
        ) {
            Children(
                modifier = modifier.fillMaxSize(),
                stack = backStack,
                animation = animationDefault,
                content = {
                    val entry = it.instance as NavBackStackEntry
                    // WARNING : Shared Element Transitions are not supported within the children function.
                    AnimatedContent(
                        targetState = entry,
                        contentKey = { entry.id }
                    ) { entry.destination.content(this, entry) }
                }
            )
        }
    } else {
        ChildStack(
            modifier = modifier.fillMaxSize(),
            stack = backStack,
            animation = animationExperimental,
            content = {
                val entry = it.instance as NavBackStackEntry
                entry.destination.content(this, entry)
            }
        )
    }
}