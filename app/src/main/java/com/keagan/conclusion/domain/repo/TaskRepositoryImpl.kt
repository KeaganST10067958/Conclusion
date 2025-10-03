package com.keagan.conclusion.data.repo

import com.keagan.conclusion.data.local.TaskDao
import com.keagan.conclusion.data.local.TaskEntity
import com.keagan.conclusion.domain.model.Task
import com.keagan.conclusion.domain.model.TaskStatus
import com.keagan.conclusion.domain.repo.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.util.UUID

class TaskRepositoryImpl(
    private val dao: TaskDao
) : TaskRepository {

    private fun TaskEntity.toDomain() = Task(
        id = id,
        userId = userId,
        title = title,
        status = TaskStatus.valueOf(status),
        createdAt = Instant.parse(createdAt),
        updatedAt = Instant.parse(updatedAt)
    )

    private fun Task.toEntity(): TaskEntity = TaskEntity(
        id = if (id.isBlank()) UUID.randomUUID().toString() else id,
        userId = userId,
        title = title,
        status = status.name,
        createdAt = createdAt.toString(),
        updatedAt = Instant.now().toString()
    )

    override fun observe(status: TaskStatus): Flow<List<Task>> =
        dao.observeByStatus(status.name).map { list -> list.map { it.toDomain() } }

    override suspend fun upsert(task: Task) {
        dao.upsert(task.toEntity())
    }

    override suspend fun move(id: String, to: TaskStatus) {
        val e = dao.find(id) ?: return
        dao.upsert(e.copy(status = to.name, updatedAt = Instant.now().toString()))
    }

    override suspend fun delete(id: String) {
        dao.find(id)?.let { dao.delete(it) }
    }
}
