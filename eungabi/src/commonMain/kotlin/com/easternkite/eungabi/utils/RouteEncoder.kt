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

const val ENCODED_AMPERSAND = "^eungabi^"

/**
 * [RouteEncoder] is designed to handle exceptions when query parameter values contain ampersands.
 * For example, this can occur when a query parameter value includes a URL.
 *
 * It provides functions to encode and decode routes and argument values,
 * ensuring that ampersands within query parameters are handled correctly
 * to avoid parsing errors or unexpected behavior.
 */
internal object RouteEncoder

/**
 * Encodes a full route by replacing ampersands within curly braces with a specific encode phrase.
 *
 * @param fullRoute The full route to encode.
 * @return The encoded full route.
 */
@Deprecated(
    message = "This function is problematic when handling JSON strings as arguments. " +
        "Refer to the issue for more details: https://github.com/euuungb/eungabi/issues/94",
    level = DeprecationLevel.WARNING
)
internal fun RouteEncoder.encodeFullRoute(fullRoute: String): String {
    val openBrace = '{'
    val closeBrace = '}'

    // Use a StringBuilder for efficient string manipulation.
    val stringBuilder = StringBuilder()
    // Flag to track whether we are currently inside curly braces.
    var insideBraces = false

    fullRoute.forEach { char ->
        when (char) {
            // Set the flag to true when we encounter an opening brace.
            openBrace -> insideBraces = true

            // Set the flag to false when we encounter a closing brace.
            closeBrace -> insideBraces = false

            // Replace ampersands with the encode phrase if we are inside braces, otherwise append the ampersand as is.
            '&' -> if (insideBraces) stringBuilder.append(ENCODED_AMPERSAND) else stringBuilder.append(char)

            // Append any other characters as is.
            else -> stringBuilder.append(char)
        }
    }

    return stringBuilder.toString()
}

/**
 * Decodes an argument value by replacing the encoded ampersand with the actual ampersand character.
 *
 * This function assumes that the encoded ampersand is represented by the constant [ENCODED_AMPERSAND].
 *
 * @param value The argument value to decode.
 * @return The decoded argument value.
 */
@Deprecated(
    message = "This function is problematic when handling JSON strings as arguments. " +
        "Refer to the issue for more details: https://github.com/euuungb/eungabi/issues/94",
    level = DeprecationLevel.WARNING
)
internal fun RouteEncoder.decodeArgumentValue(value: String): String {
    require(value.isNotEmpty()) { "Input value cannot be empty" }
    return value.replace(ENCODED_AMPERSAND, "&")
}
