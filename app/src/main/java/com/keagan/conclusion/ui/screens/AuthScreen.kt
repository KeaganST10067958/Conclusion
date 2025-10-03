package com.keagan.conclusion.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.keagan.conclusion.util.ServiceLocator

@Composable
fun AuthScreen(
    onSignedIn: () -> Unit
) {
    val context = LocalContext.current
    val auth = ServiceLocator.authManager

    // If the user previously signed in, skip to dashboard
    LaunchedEffect(Unit) {
        val acc = auth.lastAccount(context)
        if (acc?.idToken != null) onSignedIn()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val account = auth.parseResult(result.data)
        if (account?.idToken != null) {
            // TODO send account.idToken to backend
            onSignedIn()
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Plan-demic", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(password, { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())

            Button(
                onClick = { launcher.launch(auth.signInIntent(context)) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Continue with Google") }
        }
    }
}
