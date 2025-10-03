package com.keagan.conclusion.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keagan.conclusion.data.remote.CreateTaskRequest
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
    private val repo: TaskRepository
) : ViewModel() {

    val todo: StateFlow<List<Task>> =
        repo.observe(TaskStatus.TODO).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val doing: StateFlow<List<Task>> =
        repo.observe(TaskStatus.DOING).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val done: StateFlow<List<Task>> =
        repo.observe(TaskStatus.DONE).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val api = ServiceLocator.api
    private fun userId(): String = ServiceLocator.currentUserId()

    /** Local add (always) + best-effort POST to API */
    fun add(title: String) = viewModelScope.launch {
        val now = Instant.now()
        val local = Task(
            id = "",                  // repo will generate
            userId = userId(),
            title = title,
            status = TaskStatus.TODO,
            createdAt = now,
            updatedAt = now
        )
        repo.upsert(local) // local first (offline friendly)

        // Try to create on server; ignore failures (still have local)
        runCatching {
            api.createTask(CreateTaskRequest(userId = userId(), title = title))
        }
    }

    fun move(id: String, to: TaskStatus) = viewModelScope.launch { repo.move(id, to) }
    fun delete(id: String) = viewModelScope.launch { repo.delete(id) }

    /** Pull tasks from server and write into local Room */
    fun syncFromServer() = viewModelScope.launch {
        runCatching {
            val remote = api.listTasks(userId())
            // Map server items to local tasks and upsert
            remote.forEach { dto ->
                val t = Task(
                    id = "", // let repo generate local id
                    userId = dto.userId,
                    title = dto.title,
                    status = when (dto.status) {
                        "DOING" -> TaskStatus.DOING
                        "DONE"  -> TaskStatus.DONE
                        else    -> TaskStatus.TODO
                    },
                    createdAt = Instant.now(),
                    updatedAt = Instant.now()
                )
                repo.upsert(t)
            }
        }
    }
}
