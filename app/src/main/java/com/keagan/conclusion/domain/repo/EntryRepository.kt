package com.keagan.conclusion.domain.repo

import com.keagan.conclusion.domain.model.Entry
import com.keagan.conclusion.domain.model.EntryType
import kotlinx.coroutines.flow.Flow

interface EntryRepository {
    fun observe(type: EntryType): Flow<List<Entry>>
    suspend fun upsert(entry: Entry)
    suspend fun delete(id: String)
    suspend fun find(id: String): Entry?
}
