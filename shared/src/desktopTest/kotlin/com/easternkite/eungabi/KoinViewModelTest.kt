package com.easternkite.eungabi

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.easternkite.eungabi.navigation.EunGabiNavHost
import com.easternkite.eungabi.navigation.rememberEunGabiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import kotlin.test.Test

class KoinViewModel(
    val savedStateHandle: SavedStateHandle
) : ViewModel()

class KoinViewModelTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun koinTest() {
        runComposeUiTest {
            setContent {
                KoinApplication(
                    application = {
                        modules(
                            module {
                                viewModelOf(::KoinViewModel)
                            }
                        )
                    }
                ) {
                    val controller = rememberEunGabiController()
                    EunGabiNavHost(
                        controller = controller,
                        startDestination = "A"
                    ) {
                        composable("A") {
                            Text("A")
                            Button(
                                onClick = {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        controller.navigate("B?name=Eungabi")
                                    }
                                },
                                modifier = Modifier.testTag("AButton")
                            ) {
                                Text("B")
                            }
                        }
                        composable("B") {
                            val viewModel = koinViewModel<KoinViewModel>()
                            val saved = viewModel.savedStateHandle.getStateFlow("name", "fail").collectAsState()
                            LaunchedEffect(saved) {
                                println(saved.value)
                            }
                            Text("saved: ${saved.value}", modifier = Modifier.testTag("B"))
                        }
                    }
                }
            }
            waitForIdle()
            onNodeWithTag("AButton").performClick()
            waitForIdle()
            onNodeWithTag("B")
                .assertExists()
                .assertTextEquals("saved: Eungabi")
        }
    }
}
