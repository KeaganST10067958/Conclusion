package com.keagan.conclusion.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.keagan.conclusion.ui.theme.*

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 22.dp,
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Surface(
        modifier = modifier
            .shadow(elevation = 6.dp, shape = shape, clip = false)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        SurfaceGlass.copy(alpha = 0.85f),
                        SurfaceGlass.copy(alpha = 0.65f)
                    )
                )
            ),
        color = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        shape = shape,
        border = BorderStroke(1.dp, SurfaceEdge)
    ) {
        Column(Modifier.padding(contentPadding), content = content)
    }
}

@Composable
fun GlassPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (selected) PurplePrimary.copy(alpha = 0.14f) else SurfaceGlass.copy(alpha = 0.6f)
    val fg = if (selected) PurplePrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)

    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, SurfaceEdge),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(bg, CircleShape)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text, color = fg, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun StatChip(
    title: String,
    value: String,
    progressColor: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        cornerRadius = 18.dp,
        contentPadding = 12.dp
    ) {
        Text(
            title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(6.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        Box(
            Modifier
                .height(4.dp)
                .fillMaxWidth(0.35f)
                .clip(CircleShape)
                .background(progressColor)
        )
    }
}
