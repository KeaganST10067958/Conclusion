package com.keagan.conclusion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keagan.conclusion.domain.model.Task
import com.keagan.conclusion.domain.model.TaskStatus
import com.keagan.conclusion.ui.viewmodel.TasksViewModel
import com.keagan.conclusion.util.ServiceLocator

@Composable
fun TasksScreen() {
    val vm = remember { TasksViewModel(ServiceLocator.taskRepo) }
    val todo by vm.todo.collectAsState()
    val doing by vm.doing.collectAsState()
    val done by vm.done.collectAsState()

    var newTitle by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Tasks", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = newTitle, onValueChange = { newTitle = it },
                label = { Text("Add a task…") }, modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (newTitle.isNotBlank()) { vm.add(newTitle.trim()); newTitle = "" }
                },
                modifier = Modifier.height(56.dp)
            ) { Text("Add") }
        }

        Spacer(Modifier.height(4.dp))

        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TaskColumn(
                title = "To-Do",
                itemsList = todo,
                onMoveRight = { vm.move(it.id, TaskStatus.DOING) },
                onDelete = { vm.delete(it.id) },
                modifier = Modifier.weight(1f) // <— weight belongs here
            )
            TaskColumn(
                title = "Doing",
                itemsList = doing,
                onMoveLeft = { vm.move(it.id, TaskStatus.TODO) },
                onMoveRight = { vm.move(it.id, TaskStatus.DONE) },
                onDelete = { vm.delete(it.id) },
                modifier = Modifier.weight(1f)
            )
            TaskColumn(
                title = "Done",
                itemsList = done,
                onMoveLeft = { vm.move(it.id, TaskStatus.DOING) },
                onDelete = { vm.delete(it.id) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TaskColumn(
    title: String,
    itemsList: List<Task>,
    onMoveLeft: ((Task) -> Unit)? = null,
    onMoveRight: ((Task) -> Unit)? = null,
    onDelete: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier.fillMaxHeight()) {
        Column(Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(itemsList, key = { it.id }) { t ->
                    ElevatedCard {
                        Column(Modifier.padding(12.dp)) {
                            Text(t.title, style = MaterialTheme.typography.bodyLarge)
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (onMoveLeft != null) {
                                    TextButton(onClick = { onMoveLeft(t) }) { Text("←") }
                                }
                                if (onMoveRight != null) {
                                    TextButton(onClick = { onMoveRight(t) }) { Text("→") }
                                }
                                Spacer(Modifier.weight(1f))
                                TextButton(onClick = { onDelete(t) }) { Text("Delete") }
                            }
                        }
                    }
                }
            }
        }
    }
}
