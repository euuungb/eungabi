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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified VM : ViewModel> eunGabiViewModel(noinline initializer: CreationExtras.(savedStateHandle: SavedStateHandle) -> VM): VM {
    val owner = LocalViewModelStoreOwner.current
        ?: error("No ViewModelStoreOwner was provided via LocalViewModelStoreOwner")

    if (owner !is EunGabiEntry) {
        error("Provided ViewModelStoreOwner was not an EunGabiEntry")
    }

    val savedStateHandle = owner.savedStateHandle
    return viewModel(
        key = owner.id,
        viewModelStoreOwner = owner
    ) {
        initializer(this, savedStateHandle)
    }
}
