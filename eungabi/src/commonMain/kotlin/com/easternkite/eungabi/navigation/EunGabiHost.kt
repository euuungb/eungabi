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
package com.easternkite.eungabi.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@Composable
expect fun EunGabiNavHost(
    modifier: Modifier = Modifier,
    controller: EunGabiController = rememberEunGabiController(),
    startDestination: String = "",
    transitionState: EunGabiTransitionState = EunGabiTransitionState(),
    predictiveBackTransition: EunGabiPredictiveState = EunGabiPredictiveState(),
    builder: EunGabiGraphBuilder.() -> Unit
)

@Composable
internal fun EunGabiNavHostInternal(
    modifier: Modifier = Modifier,
    navTransition: EunGabiTransitionState = EunGabiTransitionState(),
    predictiveBackTransition: EunGabiPredictiveState = EunGabiPredictiveState(),
    progress: Float = 0f,
    inPredictiveBack: Boolean = false,
    startDestination: String = "",
    controller: EunGabiController = rememberEunGabiController(),
    onTransitionRunning: (Boolean) -> Unit  = {},
    builder: EunGabiGraphBuilder.() -> Unit
) {
    val backStack by controller.backStack.collectAsState()
    val entity = remember(backStack) { backStack.lastOrNull() }
    val updatedTransitionRunning by rememberUpdatedState(onTransitionRunning)

    val currentTransition = remember(
        predictiveBackTransition,
        navTransition,
        inPredictiveBack,
        controller.isPop.value
    ) {
        val isPop = controller.isPop.value
        val (enter, exit) = when {
            inPredictiveBack -> predictiveBackTransition.popEnter to predictiveBackTransition.popExit
            isPop -> navTransition.popEnter to navTransition.popExit
            else -> (navTransition.enter to navTransition.exit)
        }
        enter to exit
    }

    remember(
        controller,
        startDestination
    ) {
        controller.graph = EunGabiGraphBuilder()
            .apply(builder)
            .also { it.startDestination = startDestination }
            .build()
    }

    if (entity == null) return

    val transitionState = remember {
        SeekableTransitionState(entity)
    }

    val transition = rememberTransition(transitionState, label = "entity")
    val isTransitionRunning by derivedStateOf {
        val targetProgress = if (inPredictiveBack) { progress }
        else { transitionState.fraction }

        targetProgress > 0 && targetProgress < 1
    }

    LaunchedEffect(isTransitionRunning) {
        updatedTransitionRunning(isTransitionRunning)
    }

    var previousEntry by remember { mutableStateOf<EunGabiEntry?>(null) }

    LaunchedEffect(backStack) {
        println(backStack.map { it.eunGabiDestination.route })
    }

    if (inPredictiveBack) {
        LaunchedEffect(entity) {
            println("entity changed: ${entity.eunGabiDestination.route}")
            previousEntry = controller.findPreviousEntity(entity)
        }
        LaunchedEffect(progress) {
            previousEntry?.also { transitionState.seekTo(progress, it) }
        }
    } else {
        LaunchedEffect(entity) {
            if (transitionState.currentState != entity) {
                transitionState.animateTo(entity)
            } else {
                val totalDuration = transition.totalDurationNanos / 1_000_000
                animate(
                    transitionState.fraction,
                    0f,
                    animationSpec = tween((transitionState.fraction * totalDuration).toInt())
                ) { value, _ ->
                    launch {
                        if (value > 0) {
                            transitionState.seekTo(value)
                        }

                        if (value == 0f) {
                            transitionState.seekTo(0f)
                            transitionState.snapTo(entity)
                        }
                    }
                }
            }
        }
    }

    val dimAlpha by animateFloatAsState(
        targetValue = 0.07f * (1 - if (inPredictiveBack) progress else transitionState.fraction) // progress가 1일 때 0f, 0일 때 0.05f
    )
    transition.AnimatedContent(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentKey = {
            // Ordering-Based Keying Strategy to assume screen is currently pushing or popping.
            if (it.index > entity.index) {
                // Case1 : targetState is Higher index than currentEntry. (assuming screen is currently pushing)
                entity.id + it.id
            } else {
                // Case2 : targetState is Lower than currentEntry. (assuming screen is currently popping)
                it.id + entity.id
            }
        },
        transitionSpec = {
            val (enter, exit) = currentTransition
            ContentTransform(enter(this), exit(this), targetState.index.toFloat())
        }
    ) { targetState ->
        InputBlockingLayer(isTransitionRunning) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawContent()
                        // Enable background dimming when the transition is running. This will apply to the previous screen.
                        // it currently supports only predictive back gesturing.
                        if (!inPredictiveBack) return@drawWithContent
                        if (targetState.index < entity.index || targetState.index < transition.currentState.index) {
                            drawRect(color = Color.Black.copy(dimAlpha))
                        }
                    }
            ) {
                controller
                    .graph
                    .findDestination(targetState.eunGabiDestination.fullRoute)
                    .content(this@AnimatedContent, targetState)
            }
        }
    }
}

/**
 * Prevent User Input when transition is running.
 */
@Composable fun InputBlockingLayer(
    isTransitionRunning: Boolean,
    content: @Composable () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        content()
        if (isTransitionRunning) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(enabled = false, onClick = {})
                    .background(Color.Transparent),
            )
        }
    }
}