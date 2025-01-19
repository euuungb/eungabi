/*
 * Copyright 2024-2025 easternkite
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

import androidx.core.bundle.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.enableSavedStateHandles
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import com.easternkite.eungabi.utils.randomUuid

/**
 * The EunGabi navigation entry.
 * It holds information of a backStack entry states to retrieve in [EunGabiNavHost].
 */
class EunGabiEntry(
    /**
     * The destination of the entry.
     */
    val eunGabiDestination: EunGabiDestination,
    /**
     * The unique identifier of the entry.
     */
    val id: String = randomUuid,
    /**
     * The navigation options of the entry.
     */
    val navOptions: NavOptions,
    /**
     * The arguments of the entry ot be passed to the target destination.
     */
    val arguments: NavArguments,
    /**
     * The index of the entry in the backStack.
     * it is used as a zIndex in the [EunGabiNavHost] for transition between entries.
     */
    val index: Int,
    /**
     * The [ViewModelStoreProvider] of this entiry.
     */
    private val viewModelStoreProvider: EunGabiViewModelStoreProvider? = null,
) : LifecycleOwner,
    ViewModelStoreOwner,
    HasDefaultViewModelProviderFactory,
    SavedStateRegistryOwner {
    private var _lifecycle = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private var savedStateRegistryAttached = false
    private val savedState: Bundle get() = arguments.toBundle()

    val savedStateHandle: SavedStateHandle by lazy {
        check(savedStateRegistryAttached) {
            "You can only access the SavedStateHandle after the SavedStateProvider has been attached."
        }
        check(lifecycle.currentState != Lifecycle.State.DESTROYED) {
            "You can't access the SavedStateHandle after the Lifecycle is destroyed."
        }
        val provider = ViewModelProvider.create(this, EunGabiSavedStateFactory(this))
        provider[SavedStateViewModel::class].handle.also {
            savedState.keySet().forEach { key ->
                key ?: return@forEach
                it[key] = savedState.getString(key)
            }
        }
    }

    override val lifecycle: Lifecycle
        get() = _lifecycle

    internal fun updateState() {
        if (!savedStateRegistryAttached) {
            savedStateRegistryController.performAttach()
            savedStateRegistryAttached = true
            viewModelStoreProvider?.also {
                enableSavedStateHandles()
            }
            savedStateRegistryController.performRestore(savedState)
        }
    }

    override val viewModelStore: ViewModelStore
        get() {
            check(savedStateRegistryAttached) {
                "You can only access the ViewModelStore after the SavedStateProvider has been attached."
            }
            check(lifecycle.currentState != Lifecycle.State.DESTROYED) {
                "You can't access the ViewModelStore after the Lifecycle is destroyed."
            }
            checkNotNull(viewModelStoreProvider) {
                "You can only access the ViewModelStoreProvider after it has been set."
            }
            return viewModelStoreProvider.getViewModelStore(id)
        }

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = object : ViewModelProvider.Factory {}

    override val defaultViewModelCreationExtras: CreationExtras
        get() {
            val extras = MutableCreationExtras()
            extras[SAVED_STATE_REGISTRY_OWNER_KEY] = this
            extras[VIEW_MODEL_STORE_OWNER_KEY] = this
            extras[DEFAULT_ARGS_KEY] = arguments.toBundle()
            return extras
        }

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry
}

internal expect class EunGabiSavedStateFactory(
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory

internal class SavedStateViewModel(
    val handle: SavedStateHandle
) : ViewModel()
