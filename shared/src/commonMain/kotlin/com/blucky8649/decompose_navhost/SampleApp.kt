package com.blucky8649.decompose_navhost

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
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
    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        NavHost(
            modifier = Modifier,
            animation = defaultPlatformAnimation(
                backHandler = navController.backHandler,
                onBack = navController::popBackStack
            ),
            navController = navController,
            startDestination = "main"
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
                    animatedVisibilityScope = this,
                )
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
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(Modifier.fillMaxSize()) {
        MainImage(
            animatedVisibilityScope,
            Modifier.size(100.dp)
        )
        Text(text = text)
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