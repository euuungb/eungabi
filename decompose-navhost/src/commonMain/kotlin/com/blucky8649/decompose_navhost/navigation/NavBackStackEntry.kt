package com.blucky8649.decompose_navhost.navigation

import com.blucky8649.decompose_navhost.utils.randomUuid
import com.blucky8649.decompose_navhost.utils.withScheme
import com.eygraber.uri.Uri

data class NavBackStackEntry(
    val destination: Destination,
    val id: String = randomUuid,
    val navOptions: NavOptions,
    val arguments: NavArguments
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

