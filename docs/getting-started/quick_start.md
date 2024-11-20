# Quick Start
This section describes how to quickly get started with `Eungabi`. 

## 1. Create Navigation Controller.
The navigation Controller(`EunGabiController`) holds the navigation graph and provides methods that allow your app to move between the destinations you want by controlling backStack.

To create a `EunGabiController` in composable function, call `rememberEunGabiController`

```kotlin
val controller = rememberEunGabiController()
```

!!! warning
    You must use `EunGabiController` with `EunGabiNavHost`, as described in the next section, by passing it as a parameter.

There are two key methods you should know: `navigate`, `navigateUp`  
`navigate` allows you to navigate to the next screen by adding the provided route to the back stack.  
`navigateUp` allows you to navigate to the previous screen by removing a latest entry(or entries) from the back stack.

## 2. Create a Navigation Host Composable
The `EunGabiNavHost` creates a navigation graph and displays the current entry of the back stack.
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
1. The call to the `EunGabiNavHost` composable passes a `EunGabiController` and a route for the start destination.  
2. The lambda passed to the `EunGabiNavHost` creates `EunGabiGraph` and set the graph to the `EunGabiController.graph`  
3. Each route is supplied as a `EunGabiDestination` by calling `EunGabiGraphBuilder.composable()` which adds the destination to the resulting `EunGabiGraph` described at 2.  
4. The lambda passed to `composable` is what the `EunGabiNavHost` displays for that destination.  


