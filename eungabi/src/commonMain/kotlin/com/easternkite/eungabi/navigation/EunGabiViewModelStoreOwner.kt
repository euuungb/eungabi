/*
 * Copyright 2025 easternkite
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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

internal class EunGabiViewModelStoreOwner : ViewModelStoreOwner {
    private var _viewModelStore: ViewModelStore? = null
    override val viewModelStore: ViewModelStore
        get() {
            if (_viewModelStore == null) {
                _viewModelStore = ViewModelStore()
            }
            return _viewModelStore!!
        }

    fun dispose() {
        viewModelStore.clear()
    }
}

/**
 * [rememberViewModelStoreOwner] is a composable function that provides a [androidx.lifecycle.ViewModelStoreOwner]
 * based on a custom lifecycle composition model Store. It will be the one that is responsible for the store
 * to maintain the proper ViewModel lifecycle with the proper way of lifecycleOwner.
 *
 * This composable will return a ViewModelStoreOwner that will be the composition Local Owner.
 * The [ViewModelStoreOwner] created will be stored in a [remember] state and it will be
 * disposed when the composable is removed from composition through the [DisposableEffect]
 *
 * @return A [ViewModelStoreOwner] instance that will be the composition local owner
 */
@Composable
internal fun rememberViewModelStoreOwner(key: Any? = null): ViewModelStoreOwner {
    val viewModelStoreOwner = remember(key) { EunGabiViewModelStoreOwner() }
    DisposableEffect(viewModelStoreOwner) {
        onDispose {
            viewModelStoreOwner.dispose()
        }
    }
    return viewModelStoreOwner
}
