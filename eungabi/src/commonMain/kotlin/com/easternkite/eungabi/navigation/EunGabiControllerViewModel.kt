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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.get
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass

/**
 * [EunGabiControllerViewModel] is a [ViewModel] responsible for managing [ViewModelStore]s associated
 * with EunGabi entries. It allows creating, retrieving, and clearing [ViewModelStore]s for specific
 * EunGabi entries.
 *
 * This ViewModel is designed to be used with the [EunGabiViewModelStoreProvider] interface,
 * which provides a way to associate a [ViewModelStore] with a unique identifier.
 *
 * The ViewModel maintains a map of [ViewModelStore]s, keyed by the EunGabi entry ID.
 */
class EunGabiControllerViewModel :
    ViewModel(),
    EunGabiViewModelStoreProvider {
    private val viewModelStores = mutableMapOf<String, ViewModelStore>()

    /**
     * Clears the ViewModelStore associated with the given [eunGabiEntryId].
     *
     * This function removes the ViewModelStore from the internal map of stores (`viewModelStores`)
     * using the provided [eunGabiEntryId] as the key. If a ViewModelStore was found and removed, it then
     * clears the contents of that store by calling `viewModelStore.clear()`.
     *
     * @param eunGabiEntryId The unique identifier of the ViewModelStore to clear.
     * @throws IllegalArgumentException if `eunGabiEntryId` is null or empty.
     * @throws IllegalStateException if no ViewModelStore is associated with the given `eunGabiEntryId`.
     */
    fun clear(eunGabiEntryId: String) {
        val viewModelStore = viewModelStores.remove(eunGabiEntryId)
        viewModelStore?.clear()
    }

    override fun onCleared() {
        viewModelStores.values.forEach(ViewModelStore::clear)
        viewModelStores.clear()
    }

    override fun getViewModelStore(eunGabiEntryId: String): ViewModelStore {
        var viewModelStore = viewModelStores[eunGabiEntryId]
        if (viewModelStore == null) {
            viewModelStore = ViewModelStore()
            viewModelStores[eunGabiEntryId] = viewModelStore
        }
        return viewModelStore
    }

    companion object {
        private val FACTORY: ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: KClass<T>,
                    extras: CreationExtras
                ): T = EunGabiControllerViewModel() as T
            }

        fun getInstance(viewModelStore: ViewModelStore): EunGabiControllerViewModel {
            val viewModelProvider = ViewModelProvider.create(viewModelStore, FACTORY)
            return viewModelProvider.get()
        }
    }
}
