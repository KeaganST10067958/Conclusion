package com.keagan.conclusion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keagan.conclusion.ui.components.GlassCard
import com.keagan.conclusion.ui.components.GlassPill
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun HomeScreen() {
    // simple state (replace with real sources later)
    var streak by remember { mutableStateOf(1) }
    val quote = remember { quoteOfTodayLocal() }

    // iOS-ish soft gradient
    val bg = Brush.linearGradient(
        0f to Color(0xFFF8E9FF),
        0.5f to Color(0xFFEFF1FF),
        1f to Color(0xFFEDE4FF)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .systemBarsPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GreetingHeader(name = "Keagan")

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            StatChip("Today", "4 tasks", Color(0xFFFF7DB6), Modifier.weight(1f))
            StatChip("Focus", "0 min", Color(0xFF6FB6FF), Modifier.weight(1f))
            StatChip("Streak", "$streak days", Color(0xFFFFD46A), Modifier.weight(1f))
        }

        // Tabs row (visual only here)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf("To-Do", "Schedule", "Pomodoro", "Notes").forEachIndexed { idx, label ->
                GlassPill(
                    text = label,
                    selected = idx == 0,          // make "To-Do" look selected for now
                    onClick = { /* hook up nav if you like */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                )
            }
        }

        // Streak card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 28.dp,
            contentPadding = 20.dp
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconBadge()

                    Column(Modifier.weight(1f)) {
                        Text("Daily streak", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                        Text("Keep the chain going · Goal: 30", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Button(
                        onClick = { streak += 1 },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6B5BFF),
                            contentColor = Color.White
                        )
                    ) { Text("Mark today") }
                }

                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                Text(quote, style = MaterialTheme.typography.titleMedium)
            }
        }

        // Live Pomodoro with presets
        PomodoroCard()
        Spacer(Modifier.height(12.dp))
    }
}

/* ---------- Pomodoro ---------- */

private data class PomPreset(val label: String, val seconds: Int)

@Composable
private fun PomodoroCard() {
    // Presets — tweak to your taste
    val presets = listOf(
        PomPreset("Short", 5 * 60),
        PomPreset("Medium", 25 * 60),
        PomPreset("Long", 50 * 60)
    )

    var selectedIndex by remember { mutableStateOf(1) } // default Medium (25:00)
    val selectedPreset = presets[selectedIndex]

    var total by remember { mutableStateOf(selectedPreset.seconds) }
    var left by remember { mutableStateOf(selectedPreset.seconds) }
    var running by remember { mutableStateOf(false) }

    // If preset changes, reset timer (when not running)
    LaunchedEffect(selectedIndex) {
        if (!running) {
            total = presets[selectedIndex].seconds
            left = total
        }
    }

    // Countdown loop
    LaunchedEffect(running, total) {
        while (running && left > 0) {
            delay(1000)
            left -= 1
        }
        if (left <= 0) running = false
    }

    val progress = if (total == 0) 0f else 1f - (left.toFloat() / total.toFloat())
    val timeText = "%02d:%02d".format(left / 60, left % 60)

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 28.dp,
        contentPadding = 24.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text("Pomodoro", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)

            // Preset pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                presets.forEachIndexed { i, p ->
                    GlassPill(
                        text = p.label,
                        selected = i == selectedIndex,
                        onClick = {
                            if (!running) {
                                selectedIndex = i
                                total = p.seconds
                                left = p.seconds
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                    )
                }
            }

            // Ring + time
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress },
                    strokeWidth = 14.dp,
                    modifier = Modifier.size(184.dp),
                    color = Color(0xFFB7A7FF)
                )
                // Inner soft fill for depth
                Box(
                    modifier = Modifier
                        .size(144.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                0f to Color(0xFFB7A7FF).copy(alpha = 0.18f),
                                1f to Color.Transparent
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(timeText, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Controls
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { running = !running },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (running) Color(0xFF6B5BFF) else Color(0xFFFF6B99),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(48.dp)
                ) { Text(if (running) "Pause" else "Start") }

                OutlinedButton(
                    onClick = {
                        running = false
                        total = selectedPreset.seconds
                        left = total
                    },
                    shape = CircleShape,
                    modifier = Modifier.height(48.dp)
                ) { Text("Reset") }
            }
        }
    }
}

/* ---------- Small helpers reused from your previous screen ---------- */

@Composable
private fun GreetingHeader(name: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Hi, $name", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
        }
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFFF4F2FF))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) { Text("KS", fontWeight = FontWeight.SemiBold, color = Color(0xFF3E3A7A)) }
    }
}

@Composable
private fun StatChip(
    title: String,
    value: String,
    progressColor: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier, cornerRadius = 22.dp, contentPadding = 14.dp) {
        Column {
            Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp), fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier
                    .height(5.dp)
                    .fillMaxWidth(0.35f)
                    .clip(CircleShape)
                    .background(progressColor)
            )
        }
    }
}

@Composable
private fun IconBadge() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0xFFF1ECFF)),
        contentAlignment = Alignment.Center
    ) { Text("🔥", fontSize = 18.sp) }
}

private fun quoteOfTodayLocal(): String {
    val quotes = listOf(
        "Start with the smallest possible step.",
        "Small wins compound—study 20 minutes now.",
        "Show up today. Momentum beats perfection.",
        "Future you will thank present you.",
        "Consistency turns hard into habit."
    )
    val epochDays = (System.currentTimeMillis() / (24L * 60 * 60 * 1000)).toInt()
    return quotes[abs(epochDays) % quotes.size]
}
