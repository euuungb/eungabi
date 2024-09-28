package com.blucky8649.decompose_navhost.navigation.experimental

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.blucky8649.decompose_navhost.navigation.NavGraphBuilder
import kotlinx.coroutines.CancellationException

@Composable
actual fun EunGabiNavHost(
    modifier: Modifier,
    startDestination: String,
    controller: EunGabiController,
    builder: NavGraphBuilder.() -> Unit
) {
    val backStack by controller.backStack.collectAsState()

    var inPredictiveBack by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(progress) {
        println("progress: ${progress}")
    }
    PredictiveBackHandler(backStack.size > 1) { backEvent ->
        try {
            backEvent.collect {
                inPredictiveBack = true
                progress = it.progress
            }
            inPredictiveBack = false
            controller.navigateUp()
        } catch (e: CancellationException) {
            inPredictiveBack = false
        }
    }

    EunGabiNavHostInternal(
        modifier = modifier,
        startDestination = startDestination,
        inPredictiveBack = inPredictiveBack,
        progress = progress,
        controller = controller,
        builder = builder
    )
}