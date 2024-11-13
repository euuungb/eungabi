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
 * The EunGabi Navigation Options.
 * It holds the route to be popped up.
 */
data class NavOptions(
    /**
     * The backing property for [popUpToRoute].
     *
     * The route to be popped up.
     * In the [EunGabiController.navigateUp], this is used to get previous entity.
     */
    private var _popUpToRoute: String = "",
    /**
     * The backing property for [inclusive].
     *
     * Whether the route to be popped up is inclusive.
     */
    private var _inclusive: Boolean = false
) {
    /**
     * The route to be popped up.
     * In the [EunGabiController.navigateUp], this is used to get previous entity.
     */
    val popUpToRoute get() = _popUpToRoute

    /**
     * Whether the route to be popped up is inclusive.
     */
    val inclusive get() = _inclusive
}

/**
 * Builder for [NavOptions].
 */
class NavOptionsBuilder {
    /**
     * The route to be popped up.
     * In the [EunGabiController.navigateUp], this is used to get previous entity.
     */
    private var popUpToRoute = ""

    /**
     * Whether the route to be popped up is inclusive.
     */
    private var inclusive = false

    /**
     * Sets the route to be popped up.
     * @param route The route to be popped up.
     * @param popUpToBuilder The builder class to set popUp option.
     */
    fun popUpTo(
        route: String,
        popUpToBuilder: PopUpToBuilder.() -> Unit
    ) {
        val builder = PopUpToBuilder().apply(popUpToBuilder)
        popUpToRoute = route
        inclusive = builder.inclusive
    }

    /**
     * Builds the [NavOptions].
     */
    fun build() = NavOptions(popUpToRoute, inclusive)
}
