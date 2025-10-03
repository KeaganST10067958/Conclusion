package com.keagan.conclusion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keagan.conclusion.domain.model.Task
import com.keagan.conclusion.domain.model.TaskStatus
import com.keagan.conclusion.domain.repo.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant

class TasksViewModel(
    private val repo: TaskRepository
) : ViewModel() {

    val todo: StateFlow<List<Task>> =
        repo.observe(TaskStatus.TODO).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val doing: StateFlow<List<Task>> =
        repo.observe(TaskStatus.DOING).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val done: StateFlow<List<Task>> =
        repo.observe(TaskStatus.DONE).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun add(title: String) = viewModelScope.launch {
        repo.upsert(
            Task(
                id = "",
                userId = "local",
                title = title,
                status = TaskStatus.TODO,
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )
    }

    fun move(id: String, to: TaskStatus) = viewModelScope.launch { repo.move(id, to) }
    fun delete(id: String) = viewModelScope.launch { repo.delete(id) }
}
