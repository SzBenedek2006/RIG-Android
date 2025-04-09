package dev.benedek.rig

// Deepseek
sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Settings : Screen("settings")

    companion object {
        fun fromRoute(route: String?): Screen =
            when (route?.substringBefore("/")) {
                Main.route -> Main
                Settings.route -> Settings
                null -> Main
                else -> throw IllegalArgumentException("Route $route not recognized")
            }
    }
}