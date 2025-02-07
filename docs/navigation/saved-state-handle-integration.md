# SavedStateHandle Integration

**eungabi 0.4.0** introduces a powerful SavedState mechanism, enabling seamless state preservation and data transfer in conjunction with ViewModels. With this new feature, each ViewModel can now access a `SavedStateHandle` object, a key-value map that allows you to store and retrieve data directly within the ViewModel.
Crucially, the data within the ViewModelSavedState persists even through process termination and configuration changes, ensuring that your ViewModel's state remains intact.

## Using SavedStateHandle in EunGabi

EunGabi provides several convenient ways to utilize `SavedStateHandle` for managing state across navigation events. Here are the primary methods:

### 1. Accessing SavedStateHandle via `EunGabiController`

You can access the `SavedStateHandle` of a specific `EunGabiEntry` by leveraging the `EunGabiController.backStack`. This allows you to retrieve the `SavedStateHandle` from any entry within the navigation back stack.
```kotlin
val controller = rememberEunGabiController()
val backStack by controller.backStack. collectAsState()
val handle = backStack.last().savedStateHandle val viewModel = viewModel { ViewModel(handle) }
```

**Explanation:**
*   `rememberEunGabiController()`: Retrieves the current `EunGabiController` instance.
*   `controller.backStack.collectAsState()`: Collects the current state of the back stack as a `State` object.
*   `backStack.last().savedStateHandle`: Accesses the `SavedStateHandle` of the most recent entry in the back stack.
* `viewModel { ViewModel(handle) }`: Creates a ViewModel instance with the SavedStateHandle.



### 2. Accessing SavedStateHandle within `NavGraphBuilder.composable()`

Within the content block of `NavGraphBuilder.composable()`, you can directly access the `SavedStateHandle` of the current `EunGabiEntry`.
```kotlin 
NavHost(...) { 
	composable(...) { entry -> 
		val handle = entry.savedStateHandle
		val viewModel = viewModel { ViewModel(handle) } 
	}
}
```

**Explanation:**

*   `composable(...) { entry -> ... }`: The `composable` function provides the current `EunGabiEntry` as a parameter.
*   `entry.savedStateHandle`: Directly accesses the `SavedStateHandle` associated with the current entry.
* `viewModel { ViewModel(handle) }`: Creates a ViewModel instance with the SavedStateHandle.

### 3. Using Parameter Default Values with `eunGabiViewModel()`

For a more streamlined approach, you can use the `eunGabiViewModel(...)` function to inject the `SavedStateHandle` directly into your composable function's parameters.

`eunGabiViewModel` automatically recognizes the currently active `EunGabiEntry` and provides the corresponding `SavedStateHandle`.

```kotlin
@Composable 
fun EunGabiScreen( 
	viewModel: EunGabiViewModel = eunGabiViewModel { handle -> EunGabiViewModel(handle)  }
) { ... }
```

**Explanation:**

* `eunGabiViewModel { handle -> EunGabiViewModel(handle) }`: This function automatically provides the correct `SavedStateHandle` to the lambda, which you can then use to create your `EunGabiViewModel`.
* `EunGabiViewModel(handle)`: Creates a EunGabiViewModel instance with the SavedStateHandle.

### Hilt Integration

When using Hilt in your Android target, you can seamlessly inject the appropriate `SavedStateHandle` using `hiltViewModel()`.

```kotlin
@Composable  
fun HiltView(viewModel: HiltViewModel = hiltViewModel()) {  
    val state by viewModel.state.collectAsState()  
    Text(text = state, modifier = Modifier.testTag("state"))  
}
```
**Explanation:**

* `hiltViewModel()`: This Hilt function automatically provides the correct `SavedStateHandle` to your `HiltViewModel`.
* `viewModel.state.collectAsState()`: Collects the current state of the ViewModel.

!!! tip  
    For a practical example of Hilt integration, refer to the [EunGabiHiltTest.kt](https://github.com/easternkite/eungabi/blob/main/androidApp/src/androidTest/kotlin/com/easternkite/eungabi/android/EunGabiHiltTest.kt) file in the EunGabi repository.

### Koin Integration

Similarly, when using Koin in your KMP project, you can easily inject the `SavedStateHandle` using `koinViewModel()`. Koin, like Hilt, internally recognizes the current entry and provides the correct `SavedStateHandle`.

```kotlin
@Composable  
fun KoinView(  
    viewModel: KoinViewModel = koinViewModel()  
) {  
    val state by viewModel.state.collectAsState()  
    Text(text = state, modifier = Modifier.testTag("state"))  
}
```

**Explanation:**

* `koinViewModel()`: This Koin function automatically provides the correct `SavedStateHandle` to your `KoinViewModel`.
* `viewModel.state.collectAsState()`: Collects the current state of the ViewModel.

!!! tip  
    For a practical example of Koin integration, refer to the [KoinViewModelTest.kt](https://github.com/easternkite/eungabi/blob/main/shared/src/desktopTest/kotlin/com/easternkite/eungabi/KoinViewModelTest.kt) file in the EunGabi repository.  
