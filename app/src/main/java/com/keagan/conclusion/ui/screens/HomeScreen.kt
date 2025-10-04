package com.keagan.conclusion.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.roundToInt

/* ------------------------------------------
   Lightweight “glass” components (no blur API)
------------------------------------------- */

@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 22.dp,
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val surface = MaterialTheme.colorScheme.surface
    val outline = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.linearGradient(
                    listOf(
                        surface.copy(alpha = 0.55f),
                        surface.copy(alpha = 0.35f)
                    )
                )
            )
            .border(1.dp, outline, RoundedCornerShape(cornerRadius))
            .padding(contentPadding)
    ) { content() }
}

@Composable
private fun GlassPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
        else MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
        label = "pillBg"
    )
    val fg by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "pillFg"
    )
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(bg)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text, color = fg, style = MaterialTheme.typography.labelLarge)
    }
}

/* ------------------------------------------
   Home / Dashboard screen
------------------------------------------- */

@Composable
fun HomeScreen() {
    val listState = rememberLazyListState()

    // Streak + quote – local only (no backend dependency)
    var streak by rememberSaveable { mutableIntStateOf(1) }
    val quote by remember { mutableStateOf(localQuoteOfToday()) }

    // Tabs (visual only; you can hook them later)
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("To-Do", "Schedule", "Pomodoro", "Notes")

    // Pomodoro state
    val presets = listOf(
        "Short" to 25 * 60,   // 25:00
        "Medium" to 40 * 60,  // 40:00
        "Long" to 60 * 60     // 60:00
    )
    var selectedPresetIndex by rememberSaveable { mutableIntStateOf(0) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    var remaining by rememberSaveable { mutableIntStateOf(presets[selectedPresetIndex].second) }

    // Timer tick
    LaunchedEffect(isRunning, selectedPresetIndex) {
        while (isRunning && remaining > 0) {
            delay(1000)
            remaining = max(0, remaining - 1)
        }
        if (remaining == 0) isRunning = false
    }

    // Top gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                    )
                )
            )
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            item {
                Text(
                    "Dashboard",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            /* Streak + Quote */
            item {
                GlassCard {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Whatshot,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Daily streak",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                "Keep the chain going • Goal: 30",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        FilledTonalButton(
                            onClick = {
                                // increment streak once per day (for now just +1)
                                streak = (streak + 1).coerceAtLeast(1)
                            }
                        ) { Text("Mark today") }
                    }

                    Spacer(Modifier.height(12.dp))
                    Divider()
                    Spacer(Modifier.height(12.dp))

                    Text(
                        quote,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            /* Quick stats row */
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatChip(
                        title = "Today",
                        value = "4 tasks",
                        progress = 0.35f,
                        accent = Color(0xFFEC5AAE),
                        modifier = Modifier.weight(1f)
                    )
                    StatChip(
                        title = "Focus",
                        value = "0 min",
                        progress = 0.10f,
                        accent = Color(0xFF5AA8EC),
                        modifier = Modifier.weight(1f)
                    )
                    StatChip(
                        title = "Streak",
                        value = "$streak days",
                        progress = (streak % 30) / 30f,
                        accent = Color(0xFFF5C152),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            /* Glass tab row */
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    tabs.forEachIndexed { i, label ->
                        GlassPill(
                            text = label,
                            selected = selectedTab == i,
                            onClick = { selectedTab = i }
                        )
                    }
                }
            }

            /* Quick actions */
            item {
                GlassCard {
                    Text(
                        "Quick actions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        QuickAction(
                            icon = Icons.Filled.TaskAlt,
                            label = "New task",
                            onClick = { /* TODO: open tasks */ },
                            modifier = Modifier.weight(1f)
                        )
                        QuickAction(
                            icon = Icons.Filled.CalendarToday,
                            label = "Open calendar",
                            onClick = { /* TODO: open calendar */ },
                            modifier = Modifier.weight(1f)
                        )
                        QuickAction(
                            icon = Icons.Filled.Note,
                            label = "New note",
                            onClick = { /* TODO: open notes */ },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            /* Pomodoro */
            item {
                GlassCard {
                    Text(
                        "Pomodoro",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(Modifier.height(10.dp))

                    // Preset selector
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        presets.forEachIndexed { index, (name, seconds) ->
                            val selected = index == selectedPresetIndex
                            val pillBg by animateColorAsState(
                                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)
                                else MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
                                label = "presetBg"
                            )
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(pillBg)
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                                        CircleShape
                                    )
                                    .clickable {
                                        selectedPresetIndex = index
                                        // Reset timer to new preset
                                        isRunning = false
                                        remaining = seconds
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    name,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (selected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Timer circle
                    val minutes = remaining / 60
                    val secs = remaining % 60
                    val timeText = "%02d:%02d".format(minutes, secs)
                    val progress by animateFloatAsState(
                        targetValue = 1f - (remaining.toFloat() /
                                presets[selectedPresetIndex].second.toFloat()
                                    .coerceAtLeast(1f)),
                        label = "pomProgress"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { progress },
                            strokeWidth = 10.dp,
                            modifier = Modifier.size(170.dp),
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                timeText,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                FilledIconButton(
                                    onClick = {
                                        if (remaining == 0) {
                                            // if finished, reset to preset and start
                                            remaining = presets[selectedPresetIndex].second
                                        }
                                        isRunning = !isRunning
                                    }
                                ) {
                                    Icon(
                                        if (isRunning) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                                        contentDescription = null
                                    )
                                }
                                OutlinedButton(onClick = {
                                    isRunning = false
                                    remaining = presets[selectedPresetIndex].second
                                }) { Text("Reset") }
                            }
                        }
                    }
                }
            }

            /* (Optional) tiny preview list – placeholder only */
            items(sampleTasks) { t ->
                GlassCard {
                    Text(t.title, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        t.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/* ------------------------------------------
   Helpers & small UI pieces
------------------------------------------- */

@Composable
private fun StatChip(
    title: String,
    value: String,
    progress: Float,
    accent: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier, contentPadding = 12.dp) {
        Text(
            title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(6.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.height(8.dp))
        Box(
            Modifier
                .height(5.dp)
                .fillMaxWidth()
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.25f))
        ) {
            Box(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .background(accent)
            )
        }
    }
}

@Composable
private fun QuickAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(icon, contentDescription = null)
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

private fun localQuoteOfToday(): String {
    val quotes = listOf(
        "Small wins compound—study 20 minutes now.",
        "Show up today. Momentum beats perfection.",
        "Future you will thank present you.",
        "Consistency turns hard into habit.",
        "Start with the smallest possible step.",
        "You don’t need more time, just a start.",
        "Track the streak, not the stress.",
        "Progress, not perfection.",
        "Make it easy to begin—open the book.",
        "Done is greater than perfect."
    )
    val epochDays = (System.currentTimeMillis() / (24L * 60 * 60 * 1000)).toInt()
    return quotes[kotlin.math.abs(epochDays) % quotes.size]
}

private data class DemoTask(val title: String, val subtitle: String)
private val sampleTasks = listOf(
    DemoTask("Math revision", "30 minutes • due today"),
    DemoTask("Plan essay outline", "English • due tomorrow"),
    DemoTask("Chemistry flashcards", "15 minutes • spaced repetition"),
)
