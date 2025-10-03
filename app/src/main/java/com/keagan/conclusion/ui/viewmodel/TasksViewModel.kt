package com.keagan.conclusion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keagan.conclusion.data.remote.PlannerApi
import com.keagan.conclusion.data.remote.toDto
import com.keagan.conclusion.domain.model.Task
import com.keagan.conclusion.domain.model.TaskStatus
import com.keagan.conclusion.domain.repo.TaskRepository
import com.keagan.conclusion.util.ServiceLocator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant

class TasksViewModel(
    private val repo: TaskRepository,
    private val api: PlannerApi? = null       // pass ServiceLocator.api from your screen
) : ViewModel() {

    val todo: StateFlow<List<Task>> =
        repo.observe(TaskStatus.TODO).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val doing: StateFlow<List<Task>> =
        repo.observe(TaskStatus.DOING).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val done: StateFlow<List<Task>> =
        repo.observe(TaskStatus.DONE).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private fun userId(): String = ServiceLocator.currentUserId() // "local" if not signed in

    fun add(title: String) = viewModelScope.launch {
        val task = Task(
            id = "",
            userId = userId(),
            title = title,
            status = TaskStatus.TODO,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )

        // 1) Local first (instant UX)
        repo.upsert(task)

        // 2) Fire-and-forget to server
        api?.let { runCatching { it.createTask(task.toDto()) } }
    }

    fun move(id: String, to: TaskStatus) = viewModelScope.launch { repo.move(id, to) }

    fun delete(id: String) = viewModelScope.launch { repo.delete(id) }

    /** Optional "Sync" button behavior — pulls from server and upserts locally */
    fun syncFromServer() = viewModelScope.launch {
        val uid = userId()
        val remote = api?.getTasks(uid).orEmpty()
        remote.forEach { dto ->
            repo.upsert(
                Task(
                    id = "", // let Room assign, we ignore remote _id locally
                    userId = dto.userId,
                    title = dto.title,
                    status = TaskStatus.valueOf(dto.status),
                    createdAt = Instant.now(),
                    updatedAt = Instant.now()
                )
            )
        }
    }
}
