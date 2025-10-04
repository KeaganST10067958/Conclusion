package com.keagan.conclusion.domain.repo

import com.keagan.conclusion.domain.model.Task
import com.keagan.conclusion.domain.model.TaskStatus
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observe(): Flow<List<Task>>
    suspend fun list(): List<Task>
    suspend fun add(title: String): Task
    suspend fun move(id: String, to: TaskStatus)
    suspend fun delete(id: String)
}
