package com.keagan.conclusion.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keagan.conclusion.ui.components.PlannerBottomBar
import com.keagan.conclusion.ui.navigation.Screen

@Composable
fun Dashboard(onLogout: () -> Unit) {
    val nav = rememberNavController()
    val current = nav.currentBackStackEntryAsState().value?.destination

    Scaffold(bottomBar = { PlannerBottomBar(nav, current) }) { padding ->
        NavHost(navController = nav, startDestination = Screen.Notes.route, modifier = Modifier.padding(padding)) {
            composable(Screen.Notes.route)    { NotesScreen() }
            composable(Screen.Calendar.route) { CalendarScreen() }
            composable(Screen.Tasks.route)    { TasksScreen() }
            composable(Screen.Settings.route) { SettingsScreen(onLogout) }
        }
    }
}
