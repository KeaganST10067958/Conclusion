// app/src/main/java/com/keagan/conclusion/ui/screens/TasksScreen.kt
package com.keagan.conclusion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keagan.conclusion.domain.model.TaskStatus
import com.keagan.conclusion.ui.viewmodel.TaskUi
import com.keagan.conclusion.ui.viewmodel.TasksViewModel

@Composable
fun TasksScreen(
    vm: TasksViewModel = viewModel()
) {
    var input by remember { mutableStateOf("") }

    // ✅ collect the StateFlow for composition-safe reads
    val ui by vm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
            .padding(16.dp)
    ) {
        Text("Tasks", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f), // ✅ safe (inside Row)
                placeholder = { Text("Add a task…") }
            )
            Button(
                onClick = { vm.addTask(input); input = "" },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) { Text("Add") }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ✅ ColumnCard is a RowScope extension; weight is always legal
            ColumnCard(
                title = "To-Do",
                color = Color(0xFF9FA8DA),
                tasks = ui.todo,
                onLeft = null,
                onRight = { id -> vm.moveTask(id, TaskStatus.DOING) },
                onDelete = vm::deleteTask,
                modifier = Modifier.weight(1f)
            )

            ColumnCard(
                title = "Doing",
                color = Color(0xFF80CBC4),
                tasks = ui.doing,
                onLeft = { id -> vm.moveTask(id, TaskStatus.TODO) },
                onRight = { id -> vm.moveTask(id, TaskStatus.DONE) },
                onDelete = vm::deleteTask,
                modifier = Modifier.weight(1f)
            )

            ColumnCard(
                title = "Done",
                color = Color(0xFFA5D6A7),
                tasks = ui.done,
                onLeft = { id -> vm.moveTask(id, TaskStatus.DOING) },
                onRight = null,
                onDelete = vm::deleteTask,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * ✅ RowScope extension so callers can safely pass Modifier.weight(...)
 */
@Composable
private fun RowScope.ColumnCard(
    title: String,
    color: Color,
    tasks: List<TaskUi>,
    onLeft: ((String) -> Unit)?,
    onRight: ((String) -> Unit)?,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold, color = color)
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tasks, key = { it.id }) { t ->
                    TaskRow(task = t, onLeft = onLeft, onRight = onRight, onDelete = onDelete)
                }
            }
        }
    }
}

@Composable
private fun TaskRow(
    task: TaskUi,
    onLeft: ((String) -> Unit)?,
    onRight: ((String) -> Unit)?,
    onDelete: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                task.title,
                modifier = Modifier.weight(1f), // ✅ inside Row
                style = MaterialTheme.typography.bodyMedium
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                if (onLeft != null) {
                    IconButton(onClick = { onLeft(task.id) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Move left")
                    }
                }
                if (onRight != null) {
                    IconButton(onClick = { onRight(task.id) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Move right")
                    }
                }
                IconButton(onClick = { onDelete(task.id) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
