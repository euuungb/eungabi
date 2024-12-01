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

import com.easternkite.eungabi.utils.RouteEncoder
import com.easternkite.eungabi.utils.decodeArgumentValue
import com.easternkite.eungabi.utils.encodeFullRoute
import com.easternkite.eungabi.utils.withScheme
import com.eygraber.uri.Uri

/**
 * The EunGabi navigation arguments.
 * It parses the query parameters of the destination then holds them in a [arguments].
 */
class NavArguments(
    route: String
) {
    /**
     * The [Uri] converted from the route.
     */
    internal val routeUri = Uri.parse(withScheme(RouteEncoder.encodeFullRoute(route)))

    /**
     * The actual arguments parsed from query parameters of [routeUri].
     */
    private val arguments get() =
        routeUri
            .getQueryParameterNames()
            .associateWith { routeUri.getQueryParameter(it) }

    /**
     * Returns the [String] value of the argument with the given [key].
     */
    fun getString(key: String) = arguments[key]?.let { RouteEncoder.decodeArgumentValue(it) }

    /**
     * Returns the [Int] value of the argument with the given [key].
     */
    fun getInt(key: String) = runCatching { arguments[key]?.toInt() }.getOrNull()

    /**
     * Returns the [Long] value of the argument with the given [key].
     */
    fun getLong(key: String) = runCatching { arguments[key]?.toLong() }.getOrNull()

    /**
     * Returns the [Float] value of the argument with the given [key].
     */
    fun getFloat(key: String) = runCatching { arguments[key]?.toFloat() }.getOrNull()

    /**
     * Returns the [Double] value of the argument with the given [key].
     */
    fun getDouble(key: String) = runCatching { arguments[key]?.toDouble() }.getOrNull()

    /**
     * Returns the [Boolean] value of the argument with the given [key].
     */
    fun getBoolean(key: String) = runCatching { arguments[key]?.toBoolean() }.getOrNull()
}
