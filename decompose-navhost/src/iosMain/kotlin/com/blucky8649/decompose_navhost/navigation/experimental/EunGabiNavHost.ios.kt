package com.blucky8649.decompose_navhost.navigation.experimental

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.blucky8649.decompose_navhost.navigation.NavGraphBuilder

@Composable
actual fun EunGabiNavHost(
    modifier: Modifier,
    startDestination: String,
    controller: EunGabiController,
    builder: NavGraphBuilder.() -> Unit
) {
    EunGabiNavHostInternal(
        modifier = modifier,
        startDestination = startDestination,
        controller = controller,
        builder = builder
    )
}