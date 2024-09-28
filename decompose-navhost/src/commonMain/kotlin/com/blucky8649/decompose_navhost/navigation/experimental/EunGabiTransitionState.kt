package com.blucky8649.decompose_navhost.navigation.experimental

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import com.blucky8649.decompose_navhost.navigation.NavBackStackEntry

data class EunGabiTransitionState(
    val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = { fadeIn(animationSpec = tween(700))},
    val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = { fadeOut(animationSpec = tween(700)) },
    val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = enter,
    val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = exit,
)