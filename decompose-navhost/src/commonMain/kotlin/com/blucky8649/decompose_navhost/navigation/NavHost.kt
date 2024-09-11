package com.blucky8649.decompose_navhost.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.StackAnimation as ExperimentalStackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation as DefaultStackAnimation
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState

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
private fun NavHostInternal(
    modifier: Modifier = Modifier,
    navController: NavController,
    startDestination: String,
    animationDefault: DefaultStackAnimation<Any, Any>? = null,
    animationExperimental: ExperimentalStackAnimation<Any, Any>? = null,
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

    if (animationDefault != null) {
        Children(
            modifier = modifier.fillMaxSize(),
            stack = backStack,
            animation = animationDefault,
            content = { (it.instance as Destination).content() }
        )
    } else {
        ChildStack(
            modifier = modifier.fillMaxSize(),
            stack = backStack,
            animation = animationExperimental,
            content = { (it.instance as Destination).content() }
        )
    }
}