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

import com.easternkite.eungabi.utils.randomUuid
import com.easternkite.eungabi.utils.withScheme
import com.eygraber.uri.Uri

/**
 * The EunGabi navigation entry.
 * It holds information of a backStack entry states to retrieve in [EunGabiNavHost].
 */
data class EunGabiEntry(
    /**
     * The destination of the entry.
     */
    val eunGabiDestination: EunGabiDestination,
    /**
     * The unique identifier of the entry.
     */
    val id: String = randomUuid,
    /**
     * The navigation options of the entry.
     */
    val navOptions: NavOptions,
    /**
     * The arguments of the entry ot be passed to the target destination.
     */
    val arguments: NavArguments,
    /**
     * The index of the entry in the backStack.
     * it is used as a zIndex in the [EunGabiNavHost] for transition between entries.
     */
    val index: Int
)