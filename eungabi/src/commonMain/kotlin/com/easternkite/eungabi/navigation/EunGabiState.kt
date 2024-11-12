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

    /**
     * Saves the backStack state.
     */
    fun save(backQueue: ArrayDeque<EunGabiEntry>) {
        EunGabiState.backQueue.addAll(backQueue)
    }

    /**
     * Restores the backStack state.
     */
    fun restore(): ArrayDeque<EunGabiEntry> {
        val result = ArrayDeque(backQueue.toList())
        return result.also { backQueue.clear() }
    }
}
