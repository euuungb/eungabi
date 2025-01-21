package com.easternkite.eungabi.android

import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.easternkite.eungabi.navigation.EunGabiNavHost
import com.easternkite.eungabi.navigation.rememberEunGabiController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class EunGabiHiltTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun hiltNavigationTest() {
        composeTestRule.activity.setContent {
            HiltNavHost()
        }

        composeTestRule.apply {
            waitForIdle()
            onNodeWithTag("MainButton").performClick()
            waitForIdle()
            onNodeWithTag("state").assertTextEquals("SecondState")
        }
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
fun HiltView(viewModel: HiltViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Text(text = state, modifier = Modifier.testTag("state"))
}

@dagger.hilt.android.lifecycle.HiltViewModel
class HiltViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle
    ) : ViewModel() {
        val state = savedStateHandle.getStateFlow("state", "state")
    }
