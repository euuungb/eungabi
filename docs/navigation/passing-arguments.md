# Passing Arguments

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