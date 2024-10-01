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

import com.blucky8649.eungabi.utils.randomUuid
import com.blucky8649.eungabi.utils.withScheme
import com.eygraber.uri.Uri

data class NavBackStackEntry(
    val destination: Destination,
    val id: String = randomUuid,
    val navOptions: NavOptions,
    val arguments: NavArguments,
    val index: Int
)

class NavArguments(route: String) {
    private val routeUri = Uri.parse(withScheme(route))
    private val arguments get() = routeUri.getQueryParameterNames()
        .associateWith { routeUri.getQueryParameter(it) }

    fun getString(key: String) = arguments[key]
    fun getInt(key: String) = runCatching { arguments[key]?.toInt() }.getOrNull()
    fun getLong(key: String) = runCatching { arguments[key]?.toLong() }.getOrNull()
    fun getFloat(key: String) = runCatching { arguments[key]?.toFloat() }.getOrNull()
    fun getDouble(key: String) = runCatching { arguments[key]?.toDouble() }.getOrNull()
    fun getBoolean(key: String) = runCatching { arguments[key]?.toBoolean() }.getOrNull()
}

