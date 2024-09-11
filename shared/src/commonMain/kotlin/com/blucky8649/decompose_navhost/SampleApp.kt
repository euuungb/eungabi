package com.blucky8649.decompose_navhost

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.blucky8649.decompose_navhost.navigation.NavController
import com.blucky8649.decompose_navhost.navigation.NavHost
import com.blucky8649.decompose_navhost.navigation.rememberNavController

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun SampleApp(
    componentContext: DefaultComponentContext,
    navController: NavController = rememberNavController(componentContext = componentContext)
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            animation = defaultPlatformAnimation(
                backHandler = navController.backHandler,
                onBack = { navController.popBackStack() }
            ),
            navController = navController,
            startDestination = "main"
        ) {
            composable("main") {
                MainComponent("main") {
                    navController.navigate("details")
                }
            }
            composable("details") {
                DetailsComponent("details")
            }
        }
    }
}

@Composable
fun MainComponent(
    text: String,
    onNavigateToDetails: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Text(text = text)
        Button(onClick = onNavigateToDetails) {
            Text(text = "Navigate to Details")
        }
    }
}

@Composable
fun DetailsComponent(text: String) {
    Column(Modifier.fillMaxSize()) {
        Text(text = text)
    }
}