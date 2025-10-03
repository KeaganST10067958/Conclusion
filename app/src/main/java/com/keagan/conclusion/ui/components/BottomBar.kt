package com.keagan.conclusion.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.keagan.conclusion.ui.navigation.Screen

@Composable
fun PlannerBottomBar(nav: NavHostController, current: NavDestination?) {
    val tabs = listOf(
        Screen.Home,
        Screen.Notes,
        Screen.Calendar,
        Screen.Tasks,
        Screen.Settings
    )
    NavigationBar {
        tabs.forEach { s ->
            val selected = current?.hierarchy?.any { it.route == s.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    nav.navigate(s.route) {
                        popUpTo(nav.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = when (s) {
                            Screen.Home -> Icons.Filled.Home
                            Screen.Notes -> Icons.AutoMirrored.Filled.Note
                            Screen.Calendar -> Icons.Filled.CalendarMonth
                            Screen.Tasks -> Icons.Filled.CheckCircle
                            else -> Icons.Filled.Settings
                        },
                        contentDescription = s.route
                    )
                },
                label = {
                    Text(
                        text = when (s) {
                            Screen.Home -> "Home"
                            Screen.Notes -> "Notes"
                            Screen.Calendar -> "Calendar"
                            Screen.Tasks -> "To-Do"
                            else -> "Settings"
                        }
                    )
                }
            )
        }
    }
}
