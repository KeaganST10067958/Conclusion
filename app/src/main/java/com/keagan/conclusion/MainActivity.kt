package com.keagan.conclusion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keagan.conclusion.ui.screens.AuthScreen
import com.keagan.conclusion.ui.screens.Dashboard
import com.keagan.conclusion.util.ServiceLocator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ServiceLocator.init(this)
        setContent { App() }
    }
}

@Composable
private fun App() {
    val nav: NavHostController = rememberNavController()
    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = nav, startDestination = "auth") {
            composable("auth") {
                AuthScreen(
                    onSignedIn = {
                        nav.navigate("dashboard") {
                            popUpTo("auth") { inclusive = true }
                        }
                    }
                )
            }
            composable("dashboard") {
                Dashboard(
                    onLogout = {
                        // Optional: sign out from Google too if you want
                        // ServiceLocator.authManager.signOut(LocalContext.current)
                        nav.navigate("auth") {
                            popUpTo("dashboard") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
