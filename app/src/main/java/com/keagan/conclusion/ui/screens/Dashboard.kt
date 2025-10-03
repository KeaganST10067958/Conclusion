package com.keagan.conclusion.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keagan.conclusion.ui.components.PlannerBottomBar
import com.keagan.conclusion.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(onLogout: () -> Unit) {
    val nav = rememberNavController()
    val current by nav.currentBackStackEntryAsState()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Plan-demic") }) },
        bottomBar = { PlannerBottomBar(nav, current?.destination) }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Screen.Home.route,  // NEW start tab
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable(Screen.Home.route)     { HomeScreen() }      // NEW
            composable(Screen.Notes.route)    { NotesScreen() }
            composable(Screen.Calendar.route) { CalendarScreen() }
            composable(Screen.Tasks.route)    { TasksScreen() }
            composable(Screen.Settings.route) { SettingsScreen(onLogout) }
        }
    }
}
