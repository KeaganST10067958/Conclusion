package com.keagan.conclusion.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Create an extension DataStore on Context once (recommended pattern)
private val Context.dataStore by preferencesDataStore(name = "planner_settings")

@Composable
fun SettingsScreen(onLogout: () -> Unit) {
    val context = LocalContext.current
    val ds = context.dataStore
    val darkKey = remember { booleanPreferencesKey("dark_mode") }
    val scope = rememberCoroutineScope()

    val dark by ds.data.map { prefs -> prefs[darkKey] ?: false }
        .collectAsState(initial = false)

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineLarge)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dark mode")
            Switch(
                checked = dark,
                onCheckedChange = {
                    scope.launch { ds.edit { it[darkKey] = !dark } }
                }
            )
        }

        Button(onClick = onLogout) { Text("Logout") }
    }
}
