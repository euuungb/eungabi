package com.blucky8649.decompose_navhost

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureIcon
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureOverlay
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.blucky8649.decompose_navhost.navigation.rememberNavController

@OptIn(ExperimentalDecomposeApi::class)
fun SampleViewController() = ComposeUIViewController {
    val componentContext = remember {
        DefaultComponentContext(LifecycleRegistry())
    }
    val navController = rememberNavController(componentContext)

    PredictiveBackGestureOverlay(
        backDispatcher = navController.backHandler as BackDispatcher,
        backIcon = { progress, _ ->
            PredictiveBackGestureIcon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                progress = progress,
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        SampleApp(componentContext, navController)
    }
}