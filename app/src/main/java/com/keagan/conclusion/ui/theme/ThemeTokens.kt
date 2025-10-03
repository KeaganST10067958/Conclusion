package com.keagan.conclusion.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object AppColors {
    val Pink    = Color(0xFFE45486)   // brand “Plan-demic”
    val PinkSoft= Color(0xFFFF7DA6)
    val Lilac   = Color(0xFFB8B4FF)
    val Mint    = Color(0xFFB9F4E3)
    val Sky     = Color(0xFF7AA7FF)
    val Peach   = Color(0xFFFFC97A)

    // Background wash (very light)
    val WashTop = Color(0xFFF8F4FF)
    val WashBot = Color(0xFFF3F8FF)

    // Glass helpers
    val GlassFillTop = Color.White.copy(alpha = 0.28f)
    val GlassFillBot = Color.White.copy(alpha = 0.14f)
    val GlassStroke  = Color.White.copy(alpha = 0.40f)
}

object AppDimens {
    val RadiusCard = 22.dp
    val RadiusPill = 24.dp
    val GapSmall   = 10.dp
    val Gap        = 16.dp
}
