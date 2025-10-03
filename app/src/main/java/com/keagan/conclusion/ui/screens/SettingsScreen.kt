// SettingsScreen.kt (replace file content with this enhanced version)
package com.keagan.conclusion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import coil.compose.AsyncImage
import com.keagan.conclusion.util.ServiceLocator
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val android.content.Context.dataStore by preferencesDataStore(name = "planner_settings")

@Composable
fun SettingsScreen(onLogout: () -> Unit) {
    val context = LocalContext.current
    val ds = context.dataStore
    val darkKey = remember { booleanPreferencesKey("dark_mode") }
    val scope = rememberCoroutineScope()

    val dark by ds.data.map { it[darkKey] ?: false }.collectAsState(initial = false)

    val account = remember { ServiceLocator.authManager.lastAccount(context) }
    val displayName = account?.displayName ?: "Signed in"
    val email = account?.email ?: "Google account"
    val photo = account?.photoUrl

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineLarge)

        // Profile
        ElevatedCard(Modifier.fillMaxWidth()) {
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = photo,
                    contentDescription = "Profile photo",
                    modifier = Modifier.size(52.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(displayName, style = MaterialTheme.typography.titleMedium)
                    Text(email, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Theme toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dark mode")
            Switch(
                checked = dark,
                onCheckedChange = { scope.launch { ds.edit { it[darkKey] = !dark } } }
            )
        }

        // Logout (also sign out the Google client)
        Button(
            onClick = {
                ServiceLocator.authManager.signOut(context)
                onLogout()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Logout") }
    }
}
