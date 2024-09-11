import SwiftUI
import shared

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        return true
    }
}

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
