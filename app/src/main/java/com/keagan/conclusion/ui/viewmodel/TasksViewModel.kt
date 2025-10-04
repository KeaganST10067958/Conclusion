// app/src/main/java/com/keagan/conclusion/ui/viewmodel/TasksViewModel.kt
package com.keagan.conclusion.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.keagan.conclusion.domain.model.TaskStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class TaskUi(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val status: TaskStatus = TaskStatus.TODO
)

data class TasksState(
    val loading: Boolean = false,
    val error: String? = null,
    val todo: List<TaskUi> = emptyList(),
    val doing: List<TaskUi> = emptyList(),
    val done: List<TaskUi> = emptyList()
)

class TasksViewModel : ViewModel() {

    private val _state = MutableStateFlow(TasksState())
    val state: StateFlow<TasksState> = _state

    fun addTask(title: String) {
        if (title.isBlank()) return
        val t = TaskUi(title = title.trim(), status = TaskStatus.TODO)
        _state.update { it.copy(todo = it.todo + t, error = null) }
    }

    fun deleteTask(id: String) {
        _state.update { s ->
            s.copy(
                todo = s.todo.filterNot { it.id == id },
                doing = s.doing.filterNot { it.id == id },
                done = s.done.filterNot { it.id == id }
            )
        }
    }

    fun moveTask(id: String, to: TaskStatus) {
        // pull from any column
        val all = state.value.todo + state.value.doing + state.value.done
        val found = all.find { it.id == id } ?: return
        val updated = found.copy(status = to)

        _state.update { s ->
            val base = (all - found) + updated
            s.copy(
                todo = base.filter { it.status == TaskStatus.TODO },
                doing = base.filter { it.status == TaskStatus.DOING },
                done = base.filter { it.status == TaskStatus.DONE }
            )
        }
    }
}
