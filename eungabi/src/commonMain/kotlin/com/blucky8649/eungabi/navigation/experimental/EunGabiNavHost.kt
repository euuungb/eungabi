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
package com.blucky8649.eungabi.navigation.experimental

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import com.blucky8649.eungabi.navigation.NavBackStackEntry
import com.blucky8649.eungabi.navigation.NavGraphBuilder
import kotlinx.coroutines.launch

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
    navTransition: EunGabiTransitionState = EunGabiTransitionState(),
    predictiveBackTransition: EunGabiTransitionState = navTransition.copy(
        popEnter = {
            scaleIn(initialScale = 0.9f) + slideInHorizontally(
                animationSpec = tween(100),
                initialOffsetX = { fullWidth -> -fullWidth }
            ) + fadeIn()
        },
        popExit = {
            scaleOut(targetScale = 0.9f) + slideOutHorizontally(
                animationSpec = tween(50),
                targetOffsetX = { fullWidth -> fullWidth }
            ) + fadeOut()
        }
    ),
    progress: Float = 0f,
    inPredictiveBack: Boolean = false,
    startDestination: String = "",
    controller: EunGabiController = rememberEunGabiController(),
    builder: NavGraphBuilder.() -> Unit
) {
    val scope = rememberCoroutineScope()
    val backStack by controller.backStack.collectAsState()
    val entity = backStack.lastOrNull()

    remember(
        controller,
        startDestination
    ) {
        controller.graph = NavGraphBuilder()
            .apply(builder)
            .also { it.startDestination = startDestination }
            .build()
    }

    if (entity == null) return

    val transitionState = remember {
        SeekableTransitionState(entity)
    }

    val transition = rememberTransition(transitionState, label = "entity")
    var previousEntry by remember { mutableStateOf<NavBackStackEntry?>(null) }

    LaunchedEffect(backStack) {
        println(backStack.map { it.destination.route })
    }

    if (inPredictiveBack) {
        LaunchedEffect(entity) {
            println("entity changed: ${entity.destination.route}")
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
        targetValue = 0.07f * (1 - progress) // progress가 1일 때 0f, 0일 때 0.05f
    )

    transition.AnimatedContent(
        modifier = modifier.fillMaxSize().background(Color.Cyan),
        contentKey = { it.id },
        transitionSpec = {
            val isPop = controller.isPop.value
            val (enter, exit) = when {
                inPredictiveBack -> predictiveBackTransition.popEnter to predictiveBackTransition.popExit
                isPop -> navTransition.popEnter to navTransition.popExit
                else -> navTransition.enter to navTransition.exit
            }
            ContentTransform(enter(this), exit(this), targetState.index.toFloat())
        }
    ) { targetState ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    if (targetState != entity) {
                        drawRect(color = Color.Black.copy(dimAlpha))
                    }
                }
        ) {
            controller
                .graph
                .findDestination(targetState.destination.fullRoute)
                .content(this@AnimatedContent, targetState)
        }
    }
}
