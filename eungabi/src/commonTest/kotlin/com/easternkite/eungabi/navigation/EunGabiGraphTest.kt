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
package com.easternkite.eungabi.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@Suppress("ktlint:standard:function-naming")
class EunGabiGraphTest {
    val mockGraph = EunGabiGraphBuilder()
        .apply {
            startDestination = "A"

            composable("A") {}
            composable("B") {}
            composable("C") {}
            composable("D") {}
        }.build()

    @Test
    fun GIVEN_routeA_WHEN_call_startDestination_THEN_return_A() {
        val expected = "A"
        val actual = mockGraph.startDestination
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_routeA_WHEN_call_findDestination_THEN_return_route_of_destinationA() {
        val expected = "A"
        val actual = mockGraph.findDestination("A").route
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_routeA_WHEN_call_findDestination_THEN_return_full_route_of_destinationA() {
        val expected = "navhost://A"
        val actual = mockGraph.findDestination("A").fullRoute
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_routeZ_WHEN_call_findDestination_THEN_throw_exception() {
        assertFailsWith(
            exceptionClass = IllegalStateException::class,
            message = "Destination not found",
            block = { mockGraph.findDestination("Z") }
        )
    }

    @Test
    fun WHEN_set_startDestination_to_Z_THEN_return_Z() {
        val builder = EunGabiGraphBuilder()
        builder.startDestination = "Z"
        val expected = "Z"
        val actual = builder.startDestination
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun WHEN_build_the_graph_THEN_throw_exception() {
        val builder = EunGabiGraphBuilder()
        assertFailsWith(
            exceptionClass = IllegalStateException::class,
            message = "Start destination is not set",
            block = { builder.build() }
        )
    }

    @Test
    fun GIVEN_routeA_WHEN_call_composable_and_build_the_graph_THEN_return_route_of_destinationA() {
        val builder = EunGabiGraphBuilder()
        builder.startDestination = "A"
        builder.composable("A") {}
        val expected = "A"
        val actual = builder.build().findDestination("A").route
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_routeZ_WHEN_call_composable_and_build_the_graph_THEN_throw_exception() {
        val builder = EunGabiGraphBuilder()
        builder.startDestination = "A"
        builder.composable("A") {}
        assertFailsWith(
            exceptionClass = IllegalStateException::class,
            message = "Destination not found",
            block = { builder.build().findDestination("Z") }
        )
    }
}
