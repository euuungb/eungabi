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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * The navigation container composable.
 *
 * It is the entry point of the navigation.
 *
 * It retrieves the current destination from the [EunGabiController.backStack].
 * and also process the transition between the destinations as well.
 *
 * @param modifier The modifier to apply to this layout.
 * @param controller The controller to control the navigation.
 * @param startDestination The route of the start destination.
 * @param transitionState The state of the transition.
 * @param predictiveBackTransition The state of the predictive back transition. it is invoked when the user swipes back.
 * @param builder The builder to build the navigation graph. users can add destinations to the graph with this builder.
 */
@Composable
actual fun EunGabiNavHost(
    modifier: Modifier,
    controller: EunGabiController,
    startDestination: String,
    transitionState: EunGabiTransitionState,
    predictiveBackTransition: EunGabiPredictiveState,
    builder: EunGabiGraphBuilder.() -> Unit
) {
    EunGabiNavHostInternal(
        modifier = modifier,
        startDestination = startDestination,
        navTransition = transitionState,
        predictiveBackTransition = predictiveBackTransition,
        controller = controller,
        builder = builder,
    )
}
