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

## Passing complex arguments in URLs
!!! warning
    This feature has been deprecated since eungabi version 0.4.1 due to a JSON string parsing issue. For more details, please refer to GitHub Issue [#94](https://github.com/euuungb/eungabi/issues/94).

You can pass complex arguments, such as multi-parameter URLs, by enclosing them within curly braces (`{`, `}`). This allows you to include special characters and multiple parameters within a single URL argument.

For example:
```kotlin
val fullRoute =  
    "ScreenA?url1={https://easternkite.github.io?param1=value1&param2=value2}&url2={https://easternkite.github.io?param1=value1&param2=value2}&id=123"
controller.navigate(fullRoute)

fun EunGabiGraphBuilder.aScreenRoute() {
	composable("ScreenA") {
		val id = it.arguments.getString("url1") // it will be "https://easternkite.github.io?param1=value1&param2=value2"
		Screen(id = id)
	}
}
```