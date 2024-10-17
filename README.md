# EunGabi
A Compose Multiplatform Navigation library which support Platform Native Features like Predictive Back Gesture(Android), Swipe-Back Gesture(iOS), Shared Element Transition(Common)

## Installation
```toml
[versions]
eungabi = "0.1.0-alpha01"

[libraries]
eungabi = { module = "com.easternkite.eungabi:eungabi", version.ref = "eungabi" }
```

## How to use
> You can write code simply, in a style similar to the Jetpack Navigation Compose. A detailed guide has scheduled. (comming soon 🤗 )
```kotlin
val controller = rememberEunGabiController()
EunGabiNavHost(
    modifier = Modifier,
    controller = egController,
    startDestination = "main",
) {
    composable("main") {
        MainComponent("main") {
            egController.navigate("details")
        }
    }
    composable("details") {
        DetailsComponent(
            "details",
            onNavigateBack = egController::navigateUp
        ) {
            egController.navigate("detailA")
        }
    }
}
```

## License
```
Copyright 2024 easternkite

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
