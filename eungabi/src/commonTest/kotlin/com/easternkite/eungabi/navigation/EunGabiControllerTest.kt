package com.easternkite.eungabi.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EunGabiControllerTest {
    private val mockGraph: EunGabiGraph = EunGabiGraphBuilder()
        .apply {
            startDestination = "A"

            composable("A") {}
            composable("B") {}
            composable("C") {}
            composable("D") {}
        }.build()

    private val mockController = EunGabiController()
        .apply { graph = mockGraph }

    @Test
    fun WHEN_call_navigate_without_setting_a_graph_THEN_throw_exception() {
        val controller = EunGabiController()

        assertFailsWith(
            exceptionClass = IllegalStateException::class,
            message = "Graph is not set",
            block = { controller.navigate("A") }
        )
    }

    @Test
    fun WHEN_call_size_of_backStack_THEN_return_1() {
        val expected = 1
        val actual = mockController.backStack.value.size
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_routeB_WHEN_call_navigate_THEN_backStack_size_is_2() {
        val expected = 2
        mockController.navigate("B")
        val actual = mockController.backStack.value.size
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun WHEN_call_navigateUp_THEN_return_false() {
        val expected = false
        val actual = mockController.navigateUp()
        assertEquals(expected = expected, actual = actual)
    }
}