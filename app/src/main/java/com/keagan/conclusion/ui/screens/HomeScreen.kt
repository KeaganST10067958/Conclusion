package com.keagan.conclusion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.keagan.conclusion.ui.components.StreakBanner
import com.keagan.conclusion.util.ServiceLocator

@Composable
fun HomeScreen() {
    val ctx = LocalContext.current
    val account = ServiceLocator.authManager.lastAccount(ctx)
    val name = account?.givenName ?: account?.displayName ?: "Welcome"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Hi, $name 👋", style = MaterialTheme.typography.headlineMedium)

        // Streak + quote live here
        StreakBanner(
            modifier = Modifier
                .fillMaxWidth()
        )

        // (Optional) space for quick stats/shortcuts later
        // e.g., next due task, today’s schedule, etc.
    }
}
