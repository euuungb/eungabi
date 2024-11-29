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
package com.easternkite.eungabi.navigation.utils

import com.easternkite.eungabi.utils.randomUuid
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("ktlint:standard:function-naming")
class UuidTest {
    @Test
    fun GIVEN_1000_random_uuid_when_call_randomUuid_then_return_1000_uuid() {
        val uuidSet = mutableSetOf<String>()

        repeat(1000) {
            uuidSet.add(randomUuid)
        }

        assertEquals(expected = 1000, actual = uuidSet.size)
    }
}
