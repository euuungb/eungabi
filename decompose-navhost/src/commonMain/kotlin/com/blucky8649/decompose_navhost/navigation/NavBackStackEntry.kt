package com.blucky8649.decompose_navhost.navigation

import com.blucky8649.decompose_navhost.utils.randomUuid

data class NavBackStackEntry(
    val destination: Destination,
    val id: String = randomUuid,
    val navOptions: NavOptions
)

