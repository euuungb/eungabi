package com.easternkite.eungabi.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import org.junit.Test

@Suppress("ktlint:standard:function-signature")
class EunGabiNavHostTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun navigation_test() = runComposeUiTest {
        setContent {
            val controller = rememberEunGabiController()
            EunGabiNavHost(
                controller = controller,
                startDestination = "A"
            ) {
                composable("A") {
                    Column {
                        Text("Hello A")
                        Button(
                            onClick = { controller.navigate("B") },
                            modifier = Modifier.testTag("Button_A")
                        ) {
                            Text("next")
                        }
                    }
                }
                composable("B") {
                    Column {
                        Text("Hello B")
                        Button(
                            onClick = { controller.navigate("C") },
                            modifier = Modifier.testTag("Button_B")
                        ) {
                            Text("next")
                        }
                    }
                }
                composable("C") {
                    Column {
                        Text("Hello C")
                        Button(
                            onClick = {
                                controller.navigate("D") {
                                    popUpTo("A") { inclusive = true }
                                }
                            },
                            modifier = Modifier.testTag("Button_C")
                        ) {
                            Text("next")
                        }
                    }
                }
                composable("D") {
                    Column {
                        Text("Hello D")
                        Button(
                            onClick = { controller.navigateUp() },
                            modifier = Modifier.testTag("Button_D")
                        ) {
                            Text("next")
                        }
                    }
                }
            }
        }

        waitForIdle()
        onNodeWithText("Hello A").assertIsDisplayed()
        onNodeWithTag("Button_A")
            .assertIsDisplayed()
            .performClick()

        waitForIdle()
        onNodeWithText("Hello B").assertIsDisplayed()
        onNodeWithTag("Button_B")
            .assertIsDisplayed()
            .performClick()

        waitForIdle()
        onNodeWithText("Hello C").assertIsDisplayed()
        onNodeWithTag("Button_C")
            .assertIsDisplayed()
            .performClick()

        waitForIdle()
        onNodeWithText("Hello D").assertIsDisplayed()
        onNodeWithTag("Button_D")
            .assertIsDisplayed()
            .performClick()

        waitForIdle()
        onNodeWithText("Hello A").assertIsDisplayed()
        onNodeWithTag("Button_A")
            .assertIsDisplayed()
    }
}
