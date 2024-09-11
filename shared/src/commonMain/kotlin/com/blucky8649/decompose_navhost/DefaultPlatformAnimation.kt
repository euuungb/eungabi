package com.blucky8649.decompose_navhost

import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.StackAnimation
import com.arkivanov.essenty.backhandler.BackHandler

@OptIn(ExperimentalDecomposeApi::class)
internal expect fun defaultPlatformAnimation(
    backHandler: BackHandler,
    onBack: () -> Unit
) : StackAnimation<Any, Any>