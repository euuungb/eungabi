package com.easternkite.eungabi.navigation

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

internal actual class EunGabiSavedStateFactory actual constructor(
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = SavedStateViewModel(handle) as T
}
