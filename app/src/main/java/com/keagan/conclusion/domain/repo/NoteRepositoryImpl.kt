package com.keagan.conclusion.data.repo

import com.keagan.conclusion.data.local.NoteDao
import com.keagan.conclusion.data.local.NoteEntity
import com.keagan.conclusion.domain.model.Note
import com.keagan.conclusion.domain.repo.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.util.UUID

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {

    private fun NoteEntity.toDomain() = Note(
        id = id, userId = userId, title = title, content = content,
        createdAt = Instant.parse(createdAt), updatedAt = Instant.parse(updatedAt)
    )

    private fun Note.toEntity(): NoteEntity = NoteEntity(
        id = if (id.isBlank()) UUID.randomUUID().toString() else id,
        userId = userId,
        title = title,
        content = content,
        createdAt = createdAt.toString(),
        updatedAt = Instant.now().toString()
    )

    override fun observeAll(): Flow<List<Note>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun upsert(note: Note) {
        dao.upsert(note.toEntity())
    }

    override suspend fun delete(id: String) {
        dao.find(id)?.let { dao.delete(it) }
    }

    override suspend fun find(id: String): Note? = dao.find(id)?.toDomain()
}
