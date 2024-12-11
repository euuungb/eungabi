package com.easternkite.eungabi.navigation

import android.content.Context
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.viewinterop.AndroidView
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CachedWebViewTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun cached_webView_test() {
        composeRule.setContent {
            val controller = rememberEunGabiController()
            EunGabiNavHost(
                controller = controller,
                startDestination = "A"
            ) {
                composable("A") {
                    println("it.id = ${it.id}")
                    Box {
                        WebView(
                            id = "A",
                            url = "https://www.google.com"
                        )
                        Button(
                            onClick = { controller.navigate("A?url=https://easternkite.medium.com") },
                            modifier = Modifier.testTag("Button_A")
                        ) {
                            Text("next")
                        }
                    }
                }
            }
        }
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("Button_A")
            .performClick()
        composeRule.waitForIdle()
        composeRule
            .onNodeWithTag("Button_A")
            .isDisplayed()
    }
}

@Composable
fun WebView(
    id: String,
    url: String
) {
    val context = LocalContext.current
    val webView = remember(id, url) {
        WebViewManager.getOrCreateWebView(context, id, url)
    }

    DisposableEffect(webView) {
        onDispose {
            (webView.parent as? ViewGroup)?.removeView(webView)
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            val parent = webView.parent as? ViewGroup
            parent?.removeView(webView)
            webView
        },
        update = {
            it.url ?: it.loadUrl(url)
        }
    )
}

object WebViewManager {
    private val cache = mutableMapOf<String, WebView>()

    fun getOrCreateWebView(
        context: Context,
        id: String,
        url: String
    ): WebView {
        return cache.getOrPut(id) {
            WebView(context).apply {
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                loadUrl(url)
            }
        }
    }
}