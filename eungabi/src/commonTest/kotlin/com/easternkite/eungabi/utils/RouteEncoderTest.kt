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

import com.eygraber.uri.Uri
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RouteEncoderTest {
    @Test
    fun GIVEN_full_route_with_ampersand_WHEN_call_encodeFullRoute_THEN_return_encoded_full_route() {
        val fullRoute = "route?url={easternkite.github.io?param1=value1&param2=value2}&id=123"
        val expected =
            "route?url=easternkite.github.io?param1=value1${ENCODED_AMPERSAND}param2=value2&id=123"
        val actual = RouteEncoder.encodeFullRoute(fullRoute)
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun GIVEN_full_route_WHEN_call_decodeArgumentValue_THEN_return_decoded_full_route() {
        val encoded =
            "route?url=easternkite.github.io?param1=value1${ENCODED_AMPERSAND}param2=value2&id=123"
        val expected = "route?url=easternkite.github.io?param1=value1&param2=value2&id=123"
        val actual = RouteEncoder.decodeArgumentValue(encoded)
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun GIVEN_full_route_with_multiple_url_argument_included_WHEN_call_encodeFullRoute_THEN_return_encoded_full_route() {
        val fullRoute =
            "route?url1={https://easternkite.github.io?param1=value1&param2=value2}&url2={https://easternkite.github.io?param1=value1&param2=value2}&id=123"
        val expected =
            "route?url1=https://easternkite.github.io?param1=value1${ENCODED_AMPERSAND}param2=value2&url2=https://easternkite.github.io?param1=value1${ENCODED_AMPERSAND}param2=value2&id=123"
        val actual = RouteEncoder.encodeFullRoute(fullRoute)
        assertEquals(expected = expected, actual = actual)
    }

    @Test
    @Suppress("ktlint:standard:max-line-length")
    fun GIVEN_full_route_with_multiple_url_argument_included_WHEN_call_decodeArgumentValue_THEN_return_decoded_full_route() {
        val encoded =
            "route?url1=https://easternkite.github.io?param1=value1${ENCODED_AMPERSAND}param2=value2&url2=https://easternkite.github.io?param1=value1${ENCODED_AMPERSAND}param2=value2&id=12"
        val expected =
            "route?url1=https://easternkite.github.io?param1=value1&param2=value2&url2=https://easternkite.github.io?param1=value1&param2=value2&id=12"
        val actual = RouteEncoder.decodeArgumentValue(encoded)
        assertEquals(expected = expected, actual = actual)

        val uri = Uri.parse(withScheme(encoded))
        val url1 = uri.getQueryParameter("url1")
        val url2 = uri.getQueryParameter("url2")
        val id = uri.getQueryParameter("id")

        assertEquals(
            expected = "https://easternkite.github.io?param1=value1&param2=value2",
            actual = RouteEncoder.decodeArgumentValue(url1!!)
        )
        assertEquals(
            expected = "https://easternkite.github.io?param1=value1&param2=value2",
            actual = RouteEncoder.decodeArgumentValue(url2!!)
        )
        assertEquals(expected = "12", actual = id)
    }

    @Test
    fun GIVEN_empty_value_WHEN_call_decodeArgumentValue_THEN_throw_exception() {
        val value = ""
        assertFailsWith<IllegalArgumentException>(
            message = "Input value cannot be empty",
            block = { RouteEncoder.decodeArgumentValue(value) }
        )
    }
}
