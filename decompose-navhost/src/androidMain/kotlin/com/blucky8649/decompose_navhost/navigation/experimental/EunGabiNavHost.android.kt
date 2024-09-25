package com.blucky8649.decompose_navhost.navigation.experimental

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.blucky8649.decompose_navhost.navigation.NavGraphBuilder

@Composable
actual fun EunGabiNavHost(
    modifier: Modifier,
    startDestination: String,
    controller: EunGabiController,
    builder: NavGraphBuilder.() -> Unit
) {
    println("Hi Hi Hi")
    val backStack by controller.backStack.collectAsState()
    EunGabiNavHostInternal(modifier, startDestination, controller, builder)
    LaunchedEffect(backStack.size) {
        println("Backstack size: ${backStack.size}")
    }
    BackHandler(enabled = backStack.size > 1) {
        controller.navigateUp()
    }
}