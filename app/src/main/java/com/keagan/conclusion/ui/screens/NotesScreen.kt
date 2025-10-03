package com.keagan.conclusion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keagan.conclusion.ui.viewmodel.NotesViewModel
import com.keagan.conclusion.util.ServiceLocator

@Composable
fun NotesScreen() {
    // simple manual VM wiring
    val vm = remember { NotesViewModel(ServiceLocator.entryRepo) }
    val notes by vm.notes.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Sticky Notes", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = title, onValueChange = { title = it },
            label = { Text("Title") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = content, onValueChange = { content = it },
            label = { Text("Content") }, modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            if (title.isNotBlank()) {
                vm.addNote(title, content.ifBlank { null })
                title = ""; content = ""
            }
        }) { Text("Add note") }

        Divider(Modifier.padding(vertical = 8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(notes, key = { it.id }) { n ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(n.title, style = MaterialTheme.typography.titleMedium)
                        if (!n.content.isNullOrBlank())
                            Text(n.content!!, style = MaterialTheme.typography.bodyMedium)
                        TextButton(onClick = { vm.deleteNote(n.id) }) { Text("Delete") }
                    }
                }
            }
        }
    }
}
