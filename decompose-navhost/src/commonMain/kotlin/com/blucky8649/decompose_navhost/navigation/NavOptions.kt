package com.blucky8649.decompose_navhost.navigation

data class NavOptions(
    private var _popUpToRoute: String = "",
    private var _inclusive: Boolean = false
) {
    val popUpToRoute get() = _popUpToRoute
    val inclusive get() = _inclusive
}

class NavOptionsBuilder {
    private var popUpToRoute = ""
    private var inclusive = false

    fun popUpTo(
        route: String,
        popUpToBuilder: PopUpToBuilder.() -> Unit
    ) {
        val builder = PopUpToBuilder().apply(popUpToBuilder)
        popUpToRoute = route
        inclusive = builder.inclusive
    }

    fun build() = NavOptions(popUpToRoute, inclusive)
}

