package com.easternkite.eungabi.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

    @Test
    fun GIVEN_navigationStack_with_A_B_C_WHEN_navigateUp_called_THEN_returns_true_and_current_destination_is_B() {
        val expected = "B"
        mockController.navigate("B")
        mockController.navigate("C")
        val isNavigateUpSuccess = mockController.navigateUp()
        assertEquals(true, actual = mockController.isPop.value)
        val actual = mockController.backStack.value
            .last()
            .eunGabiDestination.route
        assertEquals(expected = true, actual = isNavigateUpSuccess)
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_navigation_stack_A_B_C_with_popupto_option_WHEN_findPreviousEntry_called_THEN_returns_A() {
        val expected = "A"
        mockController.navigate("B")
        mockController.navigate("C") {
            popUpTo("A") { inclusive = false }
        }
        val lastEntry = mockController.backStack.value.last()
        val actual = mockController.findPreviousEntry(lastEntry)
        assertEquals(expected = expected, actual = actual.eunGabiDestination.route)
    }

    @Test
    fun GIVEN_navigation_stack_A_B_C_with_inclusive_popupto_option_WHEN_findPreviousEntry_called_THEN_returns_A() {
        val expected = "A"
        mockController.navigate("B")
        mockController.navigate("C") {
            popUpTo("A") { inclusive = true }
        }
        val lastEntry = mockController.backStack.value.last()
        val actual = mockController.findPreviousEntry(lastEntry)
        assertEquals(expected = expected, actual = actual.eunGabiDestination.route)
    }

    @Test
    fun GIVEN_navigation_stack_A_B_C_D_with_inclusive_popupto_option_WHEN_findPreviousEntry_called_THEN_returns_A() {
        val expected = "A"
        mockController.navigate("B")
        mockController.navigate("C")
        mockController.navigate("D") {
            popUpTo("B") { inclusive = true }
        }
        val lastEntry = mockController.backStack.value.last()
        val actual = mockController.findPreviousEntry(lastEntry)
        assertEquals(expected = expected, actual = actual.eunGabiDestination.route)
    }

    @Test
    fun GIVEN_navigation_stack_A_B_with_inclusive_popupto_option_WHEN_findPreviousEntry_called_THEN_returns_A() {
        val expected = "A"
        mockController.navigate("B") {
            popUpTo("ZZZ") { inclusive = true }
        }
        val lastEntry = mockController.backStack.value.last()
        val actual = mockController.findPreviousEntry(lastEntry)
        assertEquals(expected = expected, actual = actual.eunGabiDestination.route)
    }

    @Test
    fun GIVEN_navigation_route_Z_WHEN_navigate_called_THEN_throws_exception() {
        assertFailsWith(
            exceptionClass = IllegalStateException::class,
            message = "Destination not found",
            block = { mockController.navigate("Z") }
        )
    }

    @Test
    fun is_pop_test() {
        mockController.navigate("B")
        assertFalse(actual = mockController.isPop.value)

        mockController.navigateUp()
        assertTrue(actual = mockController.isPop.value)

        mockController.navigate("B")
        assertFalse(actual = mockController.isPop.value)
    }

    @Test
    fun save_restore_test() {
        mockController.navigate("B")
        mockController.navigate("C")
        mockController.saveState()

        val newController = EunGabiController()
        newController.restoreState()
        newController.graph = mockGraph

        val expected = mockController.backStack.value
        val actual = newController.backStack.value
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun navigate_up_test() {
        mockController.navigate("B")
        mockController.navigate("C")
        mockController.navigate("D") {
            popUpTo("A") { inclusive = true }
        }
        val isNavigateUpSuccess = mockController.navigateUp()
        assertTrue(actual = isNavigateUpSuccess)
        val expected = "A"
        val actual = mockController.backStack.value
            .last()
            .eunGabiDestination.route

        assertEquals(expected = expected, actual = actual)
        repeat(100) {
            assertFalse(mockController.navigateUp())
        }
    }
}
