package com.easternkite.eungabi.navigation.utils

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
    fun GIVEN_a_custom_schemed_route_WHEN_call_withScheme_THEN_replace_the_scheme_with_navhost() {
        val expected = "${SCHEME}eungabi"
        val actual = withScheme("easternkite://eungabi")
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_a_route_with_multiple_scheme_separator_WHEN_call_withScheme_THEN_remove_the_scheme_separator() {
        val expected = "${SCHEME}eungabi"
        val actual = withScheme("github://easternkite://eungabi")
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_a_route_contains_invalid_charactor_WHEN_call_withScheme_THEN_remove_the_invalid_charactor() {
        val expected = "${SCHEME}blank"
        val actual = withScheme("//://://://///")
        assertEquals(expected = expected, actual = actual)
    }
}
