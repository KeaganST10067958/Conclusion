package com.keagan.conclusion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keagan.conclusion.ui.screens.AuthScreen
import com.keagan.conclusion.ui.screens.Dashboard
import kotlinx.coroutines.launch

// Dummy store just to keep your earlier call-sites compiling.
// Replace with your real persistence (DataStore/Room/Firebase) when ready.
object userStore {
    suspend fun setUser(context: android.content.Context, user: String?) { /* persist user */ }
    suspend fun clear(context: android.content.Context) { /* sign out */ }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // System splash: hooks into Theme.PlanDemic.Splash and hands off to Theme.PlanDemic
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNav(
                        onAuthenticated = { displayName ->
                            // call suspend function from a coroutine
                            lifecycleScope.launch {
                                userStore.setUser(this@MainActivity, displayName)
                            }
                        },
                        onLogout = {
                            lifecycleScope.launch {
                                userStore.clear(this@MainActivity)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppNav(
    onAuthenticated: (String) -> Unit,
    onLogout: () -> Unit
) {
    val nav = rememberNavController()

    // Start directly at auth (no Compose splash route anymore).
    NavHost(
        navController = nav,
        startDestination = "auth"
    ) {
        composable("auth") {
            AuthScreen(
                onAuthenticated = { name ->
                    onAuthenticated(name)
                    nav.navigate("dashboard") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onBackToSplash = { /* no-op: splash removed */ }
            )
        }

        composable("dashboard") {
            Dashboard(
                onLogout = {
                    onLogout()
                    nav.navigate("auth") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }
    }
}
