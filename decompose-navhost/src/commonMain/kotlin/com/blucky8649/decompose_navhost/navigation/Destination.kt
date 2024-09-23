package com.blucky8649.decompose_navhost.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import com.eygraber.uri.Uri

data class Destination(
    private val name: String,
    internal val content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit = {}
) {
    val route get() = Uri.parse(name).host ?: name
    internal val fullRoute get() = name
}

