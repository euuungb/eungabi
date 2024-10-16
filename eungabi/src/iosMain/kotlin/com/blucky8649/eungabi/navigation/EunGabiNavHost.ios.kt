/*
 * Copyright 2024 easternkite
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blucky8649.eungabi.navigation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
actual fun EunGabiNavHost(
    modifier: Modifier,
    startDestination: String,
    controller: EunGabiController,
    builder: EunGabiGraphBuilder.() -> Unit
) {
    val screenWidth = LocalWindowInfo.current.containerSize.width
    val backStack by controller.backStack.collectAsState()
    val density = LocalDensity.current
    var isSwipeToBackEnabled by remember { mutableStateOf(false) }
    val anchoredDraggableState = remember {
        val anchors = DraggableAnchors {
            DragAnchors.Start at 0f
            DragAnchors.End at screenWidth.dp.value
        }
        AnchoredDraggableState(
            anchors = anchors,
            initialValue = DragAnchors.Start,
            positionalThreshold = { distance: Float -> distance},
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = exponentialDecay(
                // Optional parameters to customize the decay behavior:
                frictionMultiplier = 0.8f, // Adjusts the friction
                absVelocityThreshold = 0.1f // Minimum velocity for decay
            ),
            confirmValueChange = { true }
        )
    }
    val progress = (anchoredDraggableState.requireOffset() / screenWidth).coerceIn(0f..1f)
    val inPredictiveBack by derivedStateOf { progress > 0 && progress < 1f}

    LaunchedEffect(progress) {
        if (progress >= 1f) {
            controller.navigateUp()
            anchoredDraggableState.snapTo(DragAnchors.Start)
        }
    }

    Box(Modifier.fillMaxSize().background(Color.Red)) {
        EunGabiNavHostInternal(
            modifier = modifier,
            progress = progress,
            inPredictiveBack = inPredictiveBack && backStack.size > 1,
            startDestination = startDestination,
            navTransition = iOSTransition,
            predictiveBackTransition = EunGabiTransitionState(
                popEnter = {
                    slideInHorizontally(
                        animationSpec = tween(1000, easing = LinearEasing),
                        initialOffsetX = { fullWidth -> -fullWidth / 4 }
                    )
                },
                popExit = {
                    slideOutHorizontally(
                        tween(durationMillis = 1000, easing = LinearEasing),
                        targetOffsetX = { fullWidth -> fullWidth }
                    )
                }
            ),
            onTransitionRunning = {
                // prevent swipe-to-back when transition is running. except swiping with predictive back.
                if (inPredictiveBack) return@EunGabiNavHostInternal
                isSwipeToBackEnabled = !it
            },
            controller = controller,
            builder = builder
        )
    }

    // Swipe-To-Back Edge Area
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(20.dp)
            .background(Color.Transparent)
            .anchoredDraggable(
                state = anchoredDraggableState,
                orientation = Orientation.Horizontal,
                enabled = isSwipeToBackEnabled
            )
    )
}

val iOSTransition = EunGabiTransitionState(
    enter = {
        slideInHorizontally(
            animationSpec = tween(300,),
            initialOffsetX = { fullWidth -> fullWidth }
        )
    },
    exit = {
        slideOutHorizontally(
            tween(durationMillis = 300),
            targetOffsetX = { fullWidth -> -fullWidth / 4 }
        )
    },

    popEnter = {
        slideInHorizontally(
            animationSpec = tween(300),
            initialOffsetX = { fullWidth -> -fullWidth / 4 }
        )
    },
    popExit = {
        slideOutHorizontally(
            tween(durationMillis = 300),
            targetOffsetX = { fullWidth -> fullWidth }
        )
    }
)

enum class DragAnchors {
    Start,
    End,
}