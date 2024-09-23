package com.blucky8649.decompose_navhost.utils

import com.blucky8649.decompose_navhost.navigation.SCHEME

fun withScheme(route: String) = if (route.startsWith(SCHEME)) route else "$SCHEME$route"