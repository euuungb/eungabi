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
package com.easternkite.eungabi.utils

import com.easternkite.eungabi.navigation.SCHEME
import com.easternkite.eungabi.utils.withScheme
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("ktlint:standard:function-naming")
class NavUtilsTest {
    @Test
    fun GIVEN_a_schemed_route_WHEN_call_withScheme_THEN_return_the_same_route() {
        val expected = "${SCHEME}test"
        val actual = withScheme("navhost://test")
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_a_non_schemed_route_WHEN_call_withScheme_THEN_return_the_schemed_route() {
        val expected = "${SCHEME}test"
        val actual = withScheme("test")
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_a_blank_route_WHEN_call_withScheme_THEN_return_the_blank_route() {
        val expected = "${SCHEME}blank"
        val actual = withScheme("")
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_a_route_with_invalid_first_character_WHEN_call_withScheme_THEN_return_the_schemed_route() {
        val expected = "${SCHEME}eungabi"
        val actual = withScheme("://eungabi")
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_a_route_contains_invalid_charactor_WHEN_call_withScheme_THEN_remove_the_invalid_charactor() {
        val expected = "${SCHEME}blank"
        val actual = withScheme("//://://://///")
        assertEquals(expected = expected, actual = actual)
    }
}
