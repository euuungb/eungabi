# Animate transitions between destinations


You can customize transition animations during navigation. To do so, pass an
[`EunGabiTransitionState`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-transition-state/index.html) instance to the [`EunGabiNavHost`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-nav-host.html) as a parameter.

The following snippets demonstrate how to animate screens with a horizontal slide during navigation.

```kotlin
// 1. declare EunGabiTransitionState
val defaultTransition =  
    EunGabiTransitionState(  
        enter = {  
            slideInHorizontally(  
                animationSpec = tween(300),  
                initialOffsetX = { fullWidth -> fullWidth },  
            )  
        },  
        exit = {  
            slideOutHorizontally(  
                tween(durationMillis = 300),  
                targetOffsetX = { fullWidth -> -fullWidth / 4 },  
            )  
        },  
        popEnter = {  
            slideInHorizontally(  
                animationSpec = tween(300),  
                initialOffsetX = { fullWidth -> -fullWidth / 4 },  
            )  
        },  
        popExit = {  
            slideOutHorizontally(  
                tween(durationMillis = 300),  
                targetOffsetX = { fullWidth -> fullWidth },  
            )  
        },  
    )


// 2. pass the 'defaultDransition' to the EunGabiNavHost as a parameter.
fun App() {
	EunGabiNavHost(
		//...
		transitionState = defaultTransition
	) { ... }
}
```

The outcome is as follows:  
<img src="https://gist.github.com/user-attachments/assets/a561c994-25c3-449e-95ee-d5989bda518f" width="300">

!!! tip
    Eungabi's transition animation mechanism works almost identically to the Jetpack Navigation library. For more details, please refer to the [official docs](https://developer.android.com/develop/ui/compose/animation/quick-guide#animate-whilst) .

## Predictive back & Swipe back gesture
You can also customize the Predictive back animation, which is a navigation feature that lets users preview where the back swipe will take them. It currently supports only Android and iOS.

To do so, pass an [`EunGabiPredictiveState`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-predictive-state/index.html) instance to the [`EunGabiNavHost`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-nav-host.html) as a parameter.

### On Android
On Android, Predictive back gesture officially supports since the Android 15.

To customize predictve back animation, in `AndroidManifest.xml`, in the `<application>` tag, set the `android:enableOnBackInvokedCallback` flag to `true`.
```xml
<application
	...
	android: enableOnBackInvokedCallBack = "true"
	... >
</application>
```

And then, pass an [`EunGabiPredictiveState`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-predictive-state/index.html) instance to the [`EunGabiNavHost`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-nav-host.html) as a parameter.

```kotlin
val defaultPredictiveState = EunGabiPredictiveState(  
    popEnter = { fadeIn(animationSpec = tween(700)) },  
    popExit = { fadeOut(animationSpec = tween(700)) }
)

fun App() {
	EunGabiNavHost(
		//...
		predictiveBackTransition = defaultPredictiveState
	) { ... }
}
```

The outcome is as follows:  
<img src="https://gist.github.com/user-attachments/assets/89ff2004-2795-4a19-98d5-516fc752ebc1" width="300">

### On iOS
On iOS, **Eungabi** now supports Native-Like Swipe back transition. To do so, also pass an [`EunGabiPredictiveState`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-predictive-state/index.html) instance to the [`EunGabiNavHost`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-nav-host.html) as a parameter.

```kotlin
val iosPredictiveBack =  
    EunGabiPredictiveState(  
        popEnter = {  
            slideInHorizontally(  
                animationSpec = tween(1000, easing = LinearEasing),  
                initialOffsetX = { fullWidth -> -fullWidth / 4 },  
            )  
        },  
        popExit = {  
            slideOutHorizontally(  
                tween(durationMillis = 1000, easing = LinearEasing),  
                targetOffsetX = { fullWidth -> fullWidth },  
            )  
        },  
    )

fun App() {
	EunGabiNavHost(
		//...
		predictiveBackTransition = iosPredictiveBack
	) { ... }
}
```

!!! tip
    It is important to set the duration of each animation spec to 1000 milliseconds. The reason for this is to ensure that the screen being transitioned moves in perfect sync with the user's finger.

The outcome is as follows:  
<img src="https://gist.github.com/user-attachments/assets/4b0701c9-f19e-49bf-a124-96b0a5f91adf" width="300">


## Shared Element Transitions
**Eungabi** also supports the **Shared Element Transitions** for all supported targets.  
The [`composable()`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-graph-builder/index.html#1270496088%2FFunctions%2F-1883328705) function in `EunGabiNavHost` provides an AnimatedVisibilityScope receiver to the content composable block, enabling Shared Element Transitions. For a more detailed guide on Shared Element Transitions, see [`Shared Element Transitions`](https://developer.android.com/develop/ui/compose/animation/shared-elements).