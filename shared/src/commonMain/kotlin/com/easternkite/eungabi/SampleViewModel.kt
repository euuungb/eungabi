package com.easternkite.eungabi

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class SampleViewModel(
    val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val name = savedStateHandle.getStateFlow("id", "default")
}
