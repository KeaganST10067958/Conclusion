package com.keagan.conclusion.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keagan.conclusion.util.ServiceLocator
import kotlinx.coroutines.launch

/**
 * Compact banner you can place at the top of Home or any screen.
 * It shows the streak and a daily quote, and lets the user check in.
 */
@Composable
fun StreakBanner(
    modifier: Modifier = Modifier,
    goalDays: Int = 30
) {
    val scope = rememberCoroutineScope()

    // Streak flow
    val streak by ServiceLocator.streakManager.observe().collectAsState(initial = 0)
    val progress = (streak % goalDays).coerceAtLeast(0).toFloat() / goalDays.toFloat()
    val anim by animateFloatAsState(targetValue = progress, label = "streak-banner-progress")

    // Quote
    var quote by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        loading = true
        runCatching { ServiceLocator.api.getQuote().text }
            .onSuccess { quote = it }
            .onFailure { quote = "Start with the smallest possible step." }
        loading = false
    }

    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(54.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(progress = { anim }, strokeWidth = 6.dp)
                    Text("$streak", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Daily streak: $streak 🔥", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = quote ?: if (loading) "Loading…" else "Keep going.",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.width(8.dp))
                FilledTonalButton(
                    onClick = { scope.launch { ServiceLocator.streakManager.markTodayDone() } },
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Check-in")
                }
            }
        }
    }
}
