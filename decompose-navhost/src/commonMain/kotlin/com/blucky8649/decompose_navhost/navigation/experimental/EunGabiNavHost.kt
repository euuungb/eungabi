package com.blucky8649.decompose_navhost.navigation.experimental

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.blucky8649.decompose_navhost.navigation.NavGraphBuilder

@Composable
expect fun EunGabiNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = "",
    controller: EunGabiController = rememberEunGabiController(),
    builder: NavGraphBuilder.() -> Unit
)

@Composable
internal fun EunGabiNavHostInternal(
    modifier: Modifier = Modifier,
    startDestination: String = "",
    controller: EunGabiController = rememberEunGabiController(),
    builder: NavGraphBuilder.() -> Unit
) {
    val backStack by controller.backStack.collectAsState()
    val entity = backStack.lastOrNull()

    LaunchedEffect(entity) {
        println("entity: ${entity?.destination?.route} @@")
    }

    remember(
        controller,
        startDestination
    ) {
        controller.graph = NavGraphBuilder()
            .apply(builder)
            .also { it.startDestination = startDestination }
            .build()
    }

    LaunchedEffect(backStack) {
        println("backStack: $backStack @@")
    }

    if (entity != null) {
        AnimatedContent(
            modifier = modifier.fillMaxSize(),
            targetState = entity,
        ) {
            controller
                .graph
                .findDestination(entity.destination.fullRoute)
                .content(this, it)
        }
    }
}
