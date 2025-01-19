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

import androidx.lifecycle.ViewModelStore

/**
 * Interface for providing ViewModelStore instances associated with specific EunGabi entry IDs.
 *
 * This interface allows components to retrieve a [ViewModelStore] that is tied to a particular
 * EunGabi entry, identified by its unique [EunGabiEntry.id].  This is useful for scoping
 * ViewModels to specific logical units or navigation destinations managed by the EunGabi system.
 *
 * The [ViewModelStore] returned by [getViewModelStore] is intended to be used for storing and
 * managing ViewModels that are relevant to the given EunGabi entry. This ensures that ViewModels
 * are properly scoped and their lifecycles are managed in accordance with the EunGabi entry's
 * lifecycle.
 */
interface EunGabiViewModelStoreProvider {
    fun getViewModelStore(eunGabiEntryId: String): ViewModelStore
}
