package com.blucky8649.decompose_navhost.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable

data class Destination(
    val name: String,
    val content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit = {}
)

