package com.keagan.conclusion.ui.navigation

sealed class Screen(val route: String) {
    data object Auth      : Screen("auth")
    data object Dashboard : Screen("dashboard")

    // Bottom tabs inside Dashboard
    data object Notes    : Screen("notes")
    data object Calendar : Screen("calendar")
    data object Tasks    : Screen("tasks")
    data object Settings : Screen("settings")
}
