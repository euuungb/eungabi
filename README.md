
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
You can create Navigation graph the most simplest way.  
See [Quick Start](https://easternkite.github.io/eungabi/getting-started/quick-start/) section of our project website.

```kotlin
val controller = rememberEunGabiController()
EunGabiNavHost(
    modifier = Modifier,
    controller = controller,
    startDestination = "routeA",
) {
    composable("routeA") {
        MainComponent("routeA") {
            controller.navigate("routeB")
        }
    }
    composable("routeB") {
        DetailsComponent(
            "routeB",
            onNavigateBack = controller::navigateUp
        ) {
            egController.navigate("routeC")
        }
    }
    //...
}
```
## Passing Arguments
**Eungabi** also provides a way to pass arguments during navigation.  
See [Passing Arguments](https://easternkite.github.io/eungabi/navigation/passing-arguments/) section of our project website.

## Transition with animations
You can simply customize transition animations during animation.  
See [Transition with Animations](https://easternkite.github.io/eungabi/navigation/animate-transitions-between-destinations/) section of our project website.

<img src="https://github.com/user-attachments/assets/265d1f11-9877-4fa2-a1d3-72b13d33f1e0" width="300"/>

## Predictive Back Animation
**Eungabi** now supports The Coolest customizable Predictive Back Animation Feature on both Android and iOS!  
See [Predictive Back Animation](https://easternkite.github.io/eungabi/navigation/animate-transitions-between-destinations/#predictive-back-swipe-back-gesture) section of our project website for detailed guides.

|Android|iOS|
|------|---|
|<img src="https://github.com/user-attachments/assets/d29d6927-75f0-4c8b-826a-3b130941f78a" width="300"/>|<img src="https://github.com/user-attachments/assets/80602ba6-1ed9-4d50-88ae-602dacca76e5" width="300"/>|

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
