package com.keagan.conclusion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keagan.conclusion.util.ServiceLocator
import kotlinx.coroutines.launch

@Composable
fun NotesScreen() {
    val repo = ServiceLocator.noteRepo
    val notes by repo.observe().collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Notes", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (title.isNotBlank() || content.isNotBlank()) {
                    scope.launch { repo.addNote(title, content) }
                    title = ""
                    content = ""
                }
            }
        ) { Text("Add") }

        Spacer(Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(notes) { n ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(n.title, style = MaterialTheme.typography.titleMedium)
                        if (n.content.isNotBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Text(n.content)
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { scope.launch { repo.deleteNote(n.id) } }) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}
