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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntOffset
import com.blucky8649.eungabi.navigation.NavGraphBuilder
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun EunGabiNavHost(
    modifier: Modifier,
    startDestination: String,
    controller: EunGabiController,
    builder: NavGraphBuilder.() -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val screenWidth = LocalWindowInfo.current.containerSize.width

    EunGabiNavHostInternal(
        modifier = modifier,
        startDestination = startDestination,
        controller = controller,
        builder = builder
    )
}

fun Modifier.swipeToBack(
    onOffsetChanged: (offset: IntOffset) -> Unit
) = composed {
    val offsetX = remember { Animatable(0f) }
    LaunchedEffect(offsetX.value) {
        onOffsetChanged(IntOffset(offsetX.value.toInt(), 0))
    }

    pointerInput(Unit) {
        // Used to calculate a settling position of a fling animation.
        val decay = splineBasedDecay<Float>(this)

        coroutineScope {
            while (true) {
                // Wait for a touch down event. Track the pointerId based on the touch
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                offsetX.stop() // Add this line to cancel any on-going animations
                // Prepare for drag events and record velocity of a fling gesture
                val velocityTracker = VelocityTracker()
                // Wait for drag events.
                awaitPointerEventScope {
                    horizontalDrag(pointerId) { change ->
                        // Add these 4 lines
                        // Get the drag amount change to offset the item with
                        val horizontalDragOffset = offsetX.value + change.positionChange().x
                        // Need to call this in a launch block in order to run it separately outside of the awaitPointerEventScope
                        launch {
                            // Instantly set the Animable to the dragOffset to ensure its moving
                            // as the user's finger moves
                            offsetX.snapTo(horizontalDragOffset)
                        }
                        // Record the velocity of the drag.
                        velocityTracker.addPosition(change.uptimeMillis, change.position)

                        // Consume the gesture event, not passed to external
                        if (change.positionChange() != Offset.Zero) change.consume()

                    }
                }
                // Dragging finished. Calculate the velocity of the fling.
                val velocity = velocityTracker.calculateVelocity().x
                // Calculate where the element eventually settles after the fling animation.
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)
                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat()
                )
                launch {
                    if (targetOffsetX.absoluteValue <= size.width) {
                        // Not enough velocity; Slide back to the default position.
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        // Enough velocity to slide away the element to the edge.
                        offsetX.animateDecay(velocity, decay)
                    }
                }
            }
        }
    }
}