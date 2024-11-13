package com.easternkite.eungabi

import androidx.compose.ui.window.ComposeUIViewController

@ViewController
fun SampleViewController() =
    ComposeUIViewController {
        SampleApp()
    }

@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class ViewController
