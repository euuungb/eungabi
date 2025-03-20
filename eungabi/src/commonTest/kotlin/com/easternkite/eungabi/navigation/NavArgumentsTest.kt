package com.easternkite.eungabi.navigation

import com.eygraber.uri.Uri
import kotlin.test.Test
import kotlin.test.assertEquals

class NavArgumentsTest {
    @Test
    fun GIVEN_route_with_query_parameters_WHEN_call_routeUri_THEN_return_uri_parsed_from_route() {
        val route = "route?param1=value1&param2=value2"
        val expected = Uri.parse("navhost://route?param1=value1&param2=value2")
        val actual = NavArguments(route).routeUri
        assertEquals(expected = "navhost", actual = actual.scheme)
        assertEquals(expected = "route", actual = actual.host)
        assertEquals(expected = expected.query, actual = actual.query)
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_wrong_formatted_route_with_query_parameters_WHEN_call_routeUri_THEN_return_uri_parsed_from_route() {
        val route = "//://::::???::@#@route?param1=value1&param2"
        val expected = Uri.parse("navhost://route?param1=value1&param2")
        val actual = NavArguments(route).routeUri
        assertEquals(expected = "navhost", actual = actual.scheme)
        assertEquals(expected = "route", actual = actual.host)
        assertEquals(expected = expected.query, actual = actual.query)
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_key_of_query_parameter_WHEN_call_getString_THEN_return_value_of_query_parameter() {
        val route = "route?param1=value1&param2=value2"

        val expected = "value1"
        val actual = NavArguments(route).getString("param1")
        assertEquals(expected = expected, actual = actual)

        val expected2 = "value2"
        val actual2 = NavArguments(route).getString("param2")
        assertEquals(expected = expected2, actual = actual2)
    }

    @Test
    fun GIVEN_key_of_query_parameter_WHEN_call_getInt_THEN_return_value_of_query_parameter() {
        val route = "route?param1=1&param2=2"
        val expected = 1
        val actual = NavArguments(route).getInt("param1")
        assertEquals(expected = expected, actual = actual)

        val expected2 = 2
        val actual2 = NavArguments(route).getInt("param2")
        assertEquals(expected = expected2, actual = actual2)
    }

    @Test
    fun GIVEN_key_of_query_parameter_WHEN_call_getLong_THEN_return_value_of_query_parameter() {
        val route = "route?param1=123123123123&param2=2"
        val expected = 123123123123L
        val actual = NavArguments(route).getLong("param1")
        assertEquals(expected = expected, actual = actual)

        val expected2 = 2L
        val actual2 = NavArguments(route).getLong("param2")
        assertEquals(expected = expected2, actual = actual2)
    }

    @Test
    fun GIVEN_key_of_query_parameter_WHEN_call_getFloat_THEN_return_value_of_query_parameter() {
        val route = "route?param1=1.0&param2=2.0"
        val expected = 1.0f
        val actual = NavArguments(route).getFloat("param1")
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_key_of_query_parameter_WHEN_call_getDouble_THEN_return_value_of_query_parameter() {
        val route = "route?param1=1.0&param2=2.0"
        val expected = 1.0
        val actual = NavArguments(route).getDouble("param1")
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_key_of_query_parameter_WHEN_call_getBoolean_THEN_return_value_of_query_parameter() {
        val route = "route?param1=true&param2=false"
        val expected = true
        val actual = NavArguments(route).getBoolean("param1")
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_url_included_value_of_key_of_query_parameter_WHEN_call_getString_THEN_return_value_of_query_parameter() {
        val route = "route?url=https://easternkite.github.io/eungabi"
        val expected = "https://easternkite.github.io/eungabi"
        val actual = NavArguments(route).getString("url")
        assertEquals(expected = expected, actual = actual)
    }
}
