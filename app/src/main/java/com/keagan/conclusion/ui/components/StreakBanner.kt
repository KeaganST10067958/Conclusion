package com.keagan.conclusion.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keagan.conclusion.util.ServiceLocator

@Composable
fun StreakBanner(
    modifier: Modifier = Modifier,
    // we only SHOW on Dashboard; updating is done at app start
    touchOnAppear: Boolean = false
) {
    var streak by remember { mutableStateOf(0) }
    val quote = remember { ServiceLocator.streakManager.quoteOfToday() }

    LaunchedEffect(touchOnAppear) {
        streak = if (touchOnAppear) {
            ServiceLocator.streakManager.touchAndGetStreak()
        } else {
            ServiceLocator.streakManager.currentStreak()
        }
    }

    ElevatedCard(modifier = modifier) {
        Column(Modifier.padding(16.dp)) {
            Text("Daily streak: $streak 🔥", style = MaterialTheme.typography.titleMedium)
            Text(quote, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
