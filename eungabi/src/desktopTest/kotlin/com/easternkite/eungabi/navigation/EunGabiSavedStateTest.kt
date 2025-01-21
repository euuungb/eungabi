package com.easternkite.eungabi.navigation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.test.Test

class EunGabiSavedStateTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun savedStateTest() =
        runComposeUiTest {
            setContent {
                HiltNavHost()
            }
            waitForIdle()
            onNodeWithTag("MainButton").performClick()
            waitForIdle()
            onNodeWithTag("state").assertTextEquals("SecondState")
        }
}

@Composable
fun HiltNavHost(modifier: Modifier = Modifier) {
    val controller = rememberEunGabiController()
    EunGabiNavHost(
        modifier = modifier,
        controller = controller,
        startDestination = "Main"
    ) {
        composable("Main") {
            Button(
                onClick = { controller.navigate("Second?state=SecondState") },
                modifier = Modifier.testTag("MainButton")
            ) {
                Text("Main")
            }
        }
        composable("Second") {
            HiltView()
        }
    }
}

@Composable
fun HiltView(viewModel: TestSavedStateViewModel = eunGabiViewModel { TestSavedStateViewModel(it) }) {
    val state by viewModel.state.collectAsState()
    Text(text = state, modifier = Modifier.testTag("state"))
}

class TestSavedStateViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val state = savedStateHandle.getStateFlow("state", "state")
}
