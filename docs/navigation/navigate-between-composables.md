# Navigate between composables

## Use a EunGabiController

The [`EunGabiController`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-controller/index.html) provides ways to navigate between composables. See the [Create Navigation Controller](https://easternkite.github.io/eungabi/getting-started/quick-start/#1-create-navigation-controller) section for instructions on instantiating the [`EunGabiController`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-controller/index.html).

```kotlin
val controller = rememberEunGabiController()
```

## Navigate to a Composable
To navigate to a composable, you should call the [`EunGabiController.navigate`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-controller/index.html#1758206243%2FFunctions%2F-1883328705). `navigate()` takes a route defined in the [`EunGabiGraphBuilder`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-graph-builder/index.html) of [`EunGabiNavHost`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-nav-host.html).  See [Create a Navigation Host Composable](https://easternkite.github.io/eungabi/getting-started/quick-start/#2-create-a-navigation-host-composable) section for instructions on creating a [`EunGabiGraph`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-graph/index.html).

```kotlin
controller.navigate("ScreenA")
```

By calling this, you can navigate to the `ScreenA` route.

## Pop up to a destination
To remove destinations from the back stack when navigating from one destination to another, add a [`popUpTo()`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-nav-options-builder/index.html#-1031005298%2FFunctions%2F-1883328705) argument to the associated `navigate()` function call.

You can include an argument for the [`inclusive`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-pop-up-to-builder/index.html#696458280%2FProperties%2F-1883328705) option with a value of `true` to also pop up a destination you have specified in `popUpTo()`.

Let's assume we have screens **A, B, C, and D** in the back stack.  
The following snippets demonstrate how to pop up to the `ScreenA` when navigating up from `ScreenD`:
```kotlin
controller.navigate("ScreenD") {
	popUpTo("ScreenB") {
		inclusive = true
	}
}
```

!!! tip
    If the `inclusive` option were `false`, the result would be a pop-up to `ScreenB`.


## Navigate back
You can navigate to the previous screen by calling [`NavController.navigateUp`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-controller/index.html#-2071943207%2FFunctions%2F-1883328705). It returns a `Boolean` value  indicating the success of the back navigation.
```kotlin
controller.navigateUp()
```