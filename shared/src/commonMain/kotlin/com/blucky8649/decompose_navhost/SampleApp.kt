package com.blucky8649.decompose_navhost

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.blucky8649.decompose_navhost.navigation.NavController
import com.blucky8649.decompose_navhost.navigation.NavHost
import com.blucky8649.decompose_navhost.navigation.rememberNavController
import com.blucky8649.sample.resources.Res
import com.blucky8649.sample.resources.ic_dy
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalDecomposeApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SampleApp(
    componentContext: DefaultComponentContext,
    navController: NavController = rememberNavController(componentContext = componentContext)
) {
    var isNavGraphCreated by remember { mutableStateOf(false) }
    if (isNavGraphCreated) {
        BackStackTracker(navController)
    }
    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        NavHost(
            modifier = Modifier,
            animation = defaultPlatformAnimation(
                backHandler = navController.backHandler,
                onBack = navController::popBackStack
            ),
            navController = navController,
            startDestination = "main",
            onNavGraphCreated = { isNavGraphCreated = true }
        ) {
            composable("main") {
                MainComponent(
                    "main",
                    animatedVisibilityScope = this@composable,
                ) {
                    navController.navigate("details")
                }
            }
            composable("details") {
                DetailsComponent(
                    "details",
                    animatedVisibilityScope = this
                ) {
                    navController.navigate("detailA")
                }
            }

            composable("detailA") {
                DetailsComponent(
                    "detailA",
                    "navigate to B",
                    this
                ) {
                    navController.navigate("detailB")
                }
            }

            composable("detailB") {
                DetailsComponent(
                    "detailB",
                    "navigate to C",
                    this
                ) {
                    navController.navigate("detailC")
                }
            }

            composable("detailC") {
                DetailsComponent(
                    "detailsC",
                    "navigate to D",
                    this
                ) {
                    navController.navigate("detailD") {
                        popUpTo("detailA") {
                            inclusive = true
                        }
                    }
                }
            }

            composable("detailD") {
                DetailsComponent(
                    "detailD",
                    "Finish",
                    this
                ) {
                    navController.popBackStack()
                }
            }
        }
    }
    BackStackTracker(navController)
}

@Composable
fun BackStackTracker(navController: NavController) {
    val backStack by navController.backStack.subscribeAsState()
    val items = backStack.items.map { it.instance }
    Box(Modifier.fillMaxSize()) {
        LazyRow(Modifier.fillMaxWidth().align(Alignment.BottomCenter)) {
            items(items.size) {
                val route = items[it].destination.name
                Text(route, modifier = Modifier.background(Color.LightGray))
                Spacer(Modifier.width(10.dp))
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MainComponent(
    text: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateToDetails: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Text(text = text)
        MainImage(
            animatedVisibilityScope,
            modifier = Modifier.size(100.dp)
        )
        Button(onClick = onNavigateToDetails) {
            Text(text = "Navigate to Details")
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailsComponent(
    text: String,
    buttonText: String = "Navigate To A",
    animatedVisibilityScope: AnimatedVisibilityScope,
    onButtonClicked: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        MainImage(
            animatedVisibilityScope,
            Modifier.size(100.dp)
        )
        Text(text = text)
        Button(onClick = onButtonClicked) {
            Text(text = buttonText)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MainImage(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(Res.drawable.ic_dy),
        contentDescription = "Dy Image",
        modifier = modifier
            .sharedElement(
                rememberSharedContentState(key = "image"),
                animatedVisibilityScope = animatedVisibilityScope
            )
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}