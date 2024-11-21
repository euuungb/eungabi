
![eungabi_readme_2](https://github.com/user-attachments/assets/d5204427-b2e9-4285-bee4-2495ce31bb83)

![GitHub branch check runs](https://img.shields.io/github/check-runs/easternkite/eungabi/main)
![Dynamic TOML Badge](https://img.shields.io/badge/dynamic/toml?url=https%3A%2F%2Fraw.githubusercontent.com%2Feasternkite%2Feungabi%2Frefs%2Fheads%2Fmain%2Fgradle%2Flibs.versions.toml&query=%24.versions.kotlin&logo=Kotlin&label=Kotlin&color=purple)
![Dynamic TOML Badge](https://img.shields.io/badge/dynamic/toml?url=https%3A%2F%2Fraw.githubusercontent.com%2Feasternkite%2Feungabi%2Frefs%2Fheads%2Fmain%2Fgradle%2Flibs.versions.toml&query=%24.versions.cmp-plugin&logo=Jetpack%20Compose&label=Compose%20Multiplatform&color=blue)
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.easternkite/eungabi?link=https%3A%2F%2Fcentral.sonatype.com%2Fartifact%2Fio.github.easternkite%2Feungabi&link=https%3A%2F%2Fcentral.sonatype.com%2Fartifact%2Fio.github.easternkite%2Feungabi)


A Compose Multiplatform Navigation library which support Platform Native Features like Predictive Back Gesture(Android), Swipe-Back Gesture(iOS), Shared Element Transition(Common).  

This library adopts a screen transition pattern based on NavHost, similar to the Jetpack Navigation library. A key difference is that it allows for customization of the Predictive Back animation separately. 

Check out the [project website](https://easternkite.github.io/eungabi) for detailed guides.

## Supported targets
* `android`
* `ios`
* `web` (`IR` and `Wasm`)
* `desktop` (`jvm` and `macOS`)

## Installation
```toml
[versions]
eungabi = "<version>"

[libraries]
eungabi = { module = "io.github.easternkite:eungabi", version.ref = "eungabi" }
```

## Quick Start
> You can write code simply, in a style similar to the Jetpack Navigation Compose. A detailed guide has scheduled. (comming soon 🤗 )
```kotlin
val controller = rememberEunGabiController()
EunGabiNavHost(
    modifier = Modifier,
    controller = controller,
    startDestination = "main",
) {
    composable("main") {
        MainComponent("main") {
            controller.navigate("details")
        }
    }
    composable("details") {
        DetailsComponent(
            "details",
            onNavigateBack = controller::navigateUp
        ) {
            controller.navigate("detailA")
        }
    }
}
```
## Passing Arguments
you can simply pass arguments by adding query parameter when calling `navigate` function. For example :
```kotlin
controller.navigate("ScreenA?id=111")
```

Then, you can access the passed arguments within a composable function defined in `EungabiGraphBuilder`. For example :
```kotlin
fun EunGabiGraphBuilder.aScreenRoute() {
    composable("ScreenA") {
        val id = it.arguments.getString("id")
        Screen(id = id)
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
