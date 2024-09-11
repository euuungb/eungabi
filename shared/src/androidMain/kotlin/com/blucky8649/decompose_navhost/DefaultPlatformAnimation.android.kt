package com.blucky8649.decompose_navhost

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatable
import com.arkivanov.essenty.backhandler.BackHandler

@OptIn(ExperimentalDecomposeApi::class)
actual fun defaultPlatformAnimation(
    backHandler: BackHandler,
    onBack: () -> Unit
) = stackAnimation(
    animator = fade() + scale(),
    predictiveBackParams = {
        PredictiveBackParams(
            backHandler = backHandler,
            onBack = onBack,
            animatable = ::androidPredictiveBackAnimatable,
        )
    }
)