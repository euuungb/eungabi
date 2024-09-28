package com.blucky8649.decompose_navhost.utils

import java.util.UUID

actual val randomUuid: String get() = UUID.randomUUID().toString()