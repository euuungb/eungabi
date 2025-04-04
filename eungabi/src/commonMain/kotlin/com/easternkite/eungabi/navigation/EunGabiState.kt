/*
 * Copyright 2024-2025 easternkite
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

/**
 * the Savable object of the [EunGabiController].
 * It holds the all backStack entities of the [EunGabiController].
 *
 * It provides two method how to save and restore the backStack state.
 */
internal object EunGabiState {
    /**
     * The backStack to be save and restore.
     */
    private var backQueue = ArrayDeque<EunGabiEntry>()
    private var viewModel: EunGabiControllerViewModel? = null

    /**
     * Saves the backStack state.
     */
    fun save(state: ControllerState) {
        backQueue.addAll(state.backQueue)
        viewModel = state.viewModel
    }

    /**
     * Restores the backStack state.
     */
    fun restore(): ControllerState {
        val backQueue = ArrayDeque(backQueue.toList())
        val result = ControllerState(backQueue, viewModel)
        return result.also {
            this.backQueue.clear()
            viewModel = null
        }
    }
}

/**
 * Represents the state of the EunGabi navigation controller.
 *
 * This class holds the current back queue of navigation entries and the associated
 * [EunGabiControllerViewModel].
 *
 * @property backQueue An [ArrayDeque] representing the stack of previous navigation entries.
 *                     Each element in the queue represents a single step in the navigation history.
 * @property viewModel The associated [EunGabiControllerViewModel] which can be null if no ViewModel is used.
 */
internal data class ControllerState(
    val backQueue: ArrayDeque<EunGabiEntry>,
    val viewModel: EunGabiControllerViewModel?
)
