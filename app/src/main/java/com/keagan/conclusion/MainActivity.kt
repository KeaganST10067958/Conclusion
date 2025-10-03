package com.keagan.conclusion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Android 12+ splash; falls back gracefully on older versions
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(colorScheme = lightColorScheme()) {
                val nav = rememberNavController()

                NavHost(navController = nav, startDestination = "auth") {
                    composable("auth") { AuthScreen(
                        onLogin = { nav.navigate("dashboard") },
                        onSignup = { nav.navigate("dashboard") }
                    ) }
                    composable("dashboard") { DashboardPlaceholder(onLogout = {
                        // For now just pop back to auth
                        nav.popBackStack(route = "auth", inclusive = false)
                    }) }
                }
            }
        }
    }
}

@Composable
fun AuthScreen(
    onLogin: () -> Unit,
    onSignup: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Plan-demic", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Login") }

            TextButton(onClick = onSignup) { Text("Create account") }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onLogin, // placeholder for Google SSO later
                modifier = Modifier.fillMaxWidth()
            ) { Text("Continue with Google") }
        }
    }
}

@Composable
fun DashboardPlaceholder(onLogout: () -> Unit) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Dashboard", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(8.dp))
            Text("Placeholder – Sticky Notes, Calendar, and To-Do will go here.")
            Spacer(Modifier.height(24.dp))
            Button(onClick = onLogout) { Text("Logout") }
        }
    }
}
