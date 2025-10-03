package com.keagan.conclusion.domain.repo

import com.keagan.conclusion.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun observeAll(): Flow<List<Note>>
    suspend fun upsert(note: Note)
    suspend fun delete(id: String)
    suspend fun find(id: String): Note?
}
