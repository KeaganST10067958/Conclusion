package com.keagan.conclusion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keagan.conclusion.R

/* ------------------------------- Screen ------------------------------- */

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    onAuthenticated: (name: String) -> Unit,
    onBackToSplash: () -> Unit = {},
    onGoogleSignIn: () -> Unit = {}
) {
    val bg = Brush.linearGradient(
        listOf(
            Color(0xFFFFE7F1),
            Color(0xFFF0E9FF),
            Color(0xFFEAF4FF)
        )
    )

    var tab by remember { mutableStateOf(AuthTab.Login) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 20.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(18.dp))

            Text(
                text = if (tab == AuthTab.Login) "Welcome back" else "Create your account",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1B1527),
                modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = if (tab == AuthTab.Login) "Sign in to continue." else "Plan faster. Study smarter.",
                fontSize = 14.sp,
                color = Color(0xFF6E6784),
                modifier = Modifier.alpha(0.95f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(18.dp))

            SegmentedTabs(
                active = tab,
                onChange = { tab = it }
            )

            Spacer(Modifier.height(18.dp))

            GlassCard(Modifier.fillMaxWidth()) {
                Column(Modifier.fillMaxWidth()) {
                    if (tab == AuthTab.Register) {
                        TranslucentField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Name"
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    TranslucentField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email"
                    )
                    Spacer(Modifier.height(12.dp))

                    TranslucentField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        isPassword = true
                    )

                    Spacer(Modifier.height(18.dp))

                    GradientButton(
                        label = if (tab == AuthTab.Login) "Sign in" else "Create account",
                        loading = loading
                    ) {
                        loading = true
                        onAuthenticated(if (tab == AuthTab.Register && name.isNotBlank()) name else "User")
                        loading = false
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0x331B1527))
                Text(
                    "or",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color(0xFF6E6784),
                    fontSize = 12.sp
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0x331B1527))
            }

            GoogleGlassButton(
                text = if (tab == AuthTab.Login) "Continue with Google" else "Sign up with Google",
                onClick = onGoogleSignIn
            )

            Spacer(Modifier.height(22.dp))

            Text(
                "Back",
                color = Color(0xFF3A3357),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onBackToSplash() }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

/* ---------------------------- Building blocks ---------------------------- */

private enum class AuthTab { Login, Register }

@Composable
private fun SegmentedTabs(
    active: AuthTab,
    onChange: (AuthTab) -> Unit
) {
    val segmentBg = Color.White.copy(alpha = 0.45f)
    val chip = RoundedCornerShape(18.dp)

    Row(
        modifier = Modifier
            .clip(chip)
            .background(segmentBg)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.8f),
                        Color(0x33B388FF)
                    )
                ),
                shape = chip
            )
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabChip(tab = AuthTab.Login, active = active, label = "Log in", onChange = onChange)
        TabChip(tab = AuthTab.Register, active = active, label = "Register", onChange = onChange)
    }
}

@Composable
private fun TabChip(
    tab: AuthTab,
    active: AuthTab,
    label: String,
    onChange: (AuthTab) -> Unit
) {
    val chip = RoundedCornerShape(18.dp)
    val selected = tab == active
    Box(
        modifier = Modifier
            .clip(chip)
            .background(
                if (selected) Brush.linearGradient(
                    listOf(
                        Color(0xFFB388FF).copy(alpha = .35f),
                        Color(0xFFFF9BC7).copy(alpha = .35f)
                    )
                ) else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
            )
            .clickable { onChange(tab) }
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            color = if (selected) Color(0xFF3B2A63) else Color(0xFF6E6784),
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

@Composable
private fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(28.dp)
    Box(
        modifier = modifier
            .shadow(16.dp, shape, ambientColor = Color(0x55232340), spotColor = Color(0x55363660))
            .clip(shape)
            .background(Color.White.copy(alpha = 0.35f))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color.White.copy(alpha = 0.75f),
                        Color(0x66B388FF),
                        Color.White.copy(alpha = 0.55f)
                    )
                ),
                shape = shape
            )
            .padding(18.dp)
    ) { Column(content = content) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TranslucentField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    val shape = RoundedCornerShape(16.dp)
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0x663B2A63),
            unfocusedBorderColor = Color(0x333B2A63),
            focusedContainerColor = Color.White.copy(alpha = 0.45f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.38f),
            cursorColor = Color(0xFF6E44AA),
            focusedLabelColor = Color(0xFF6E44AA),
            unfocusedLabelColor = Color(0xFF6E6784)
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun GradientButton(
    label: String,
    loading: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    val gradient = Brush.linearGradient(
        listOf(
            Color(0xFF6D50C2),
            Color(0xFF7F4CC4),
            Color(0xFFFF7FB0)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(14.dp, shape, ambientColor = Color(0x55232340), spotColor = Color(0x55363660))
            .clip(shape)
            .background(gradient)
            .clickable(enabled = !loading) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.5.dp,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Text(label, color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun GoogleGlassButton(
    text: String,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
            .clip(shape)
            .background(Color.White.copy(alpha = 0.45f))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(Color.White.copy(alpha = 0.75f), Color(0x55B388FF))
                ),
                shape = shape
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.75f),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(28.dp)
                    .padding(4.dp)
            )
        }
        Spacer(Modifier.width(14.dp))
        Text(text = text, color = Color(0xFF3A3357), fontWeight = FontWeight.SemiBold)
    }
}
