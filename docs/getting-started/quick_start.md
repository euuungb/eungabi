# Quick Start
This section describes how to quickly get started with `Eungabi`. 

## 1. Create Navigation Controller.
The navigation Controller([`EunGabiController`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-controller/index.html)) holds the navigation graph and provides methods that allow your app to move between the destinations you want by controlling backStack.

To create a [`EunGabiController`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-controller/index.html) in composable function, call `rememberEunGabiController`

```kotlin
val controller = rememberEunGabiController()
```

!!! warning
    You must use `EunGabiController` with [`EunGabiNavHost`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-nav-host.html), as described in the next section, by passing it as a parameter.

There are two key methods you should know: [`navigate`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-controller/index.html#1758206243%2FFunctions%2F-1883328705), [`navigateUp`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-controller/index.html#-2071943207%2FFunctions%2F-1883328705)  
`navigate` allows you to navigate to the next screen by adding the provided route to the back stack.  
`navigateUp` allows you to navigate to the previous screen by removing a latest entry(or entries) from the back stack.

## 2. Create a Navigation Host Composable
The [`EunGabiNavHost`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-nav-host.html) creates a navigation graph and displays the current entry of the back stack.
By using this, you can define and control your desired routes.

you can simply call `EunGabiNavHost` in the composable function to create a navigation host.

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
!!! info "The process of displaying a screen to the user"
    1. The call to the `EunGabiNavHost` composable passes a `EunGabiController` and a route for the start destination.  
    2. The lambda passed to the `EunGabiNavHost` creates `EunGabiGraph` and set the graph to the `EunGabiController.graph`  
    3. Each route is supplied as a `EunGabiDestination` by calling [`EunGabiGraphBuilder.composable()`](https://easternkite.github.io/eungabi/api/eungabi/com.easternkite.eungabi.navigation/-eun-gabi-graph-builder/index.html#1270496088%2FFunctions%2F-1883328705) which adds the destination to the resulting `EunGabiGraph` described at 2.  
    4. The lambda passed to `composable()` is what the `EunGabiNavHost` displays for that destination.


