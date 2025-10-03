package com.keagan.conclusion.data.repo

import com.keagan.conclusion.data.local.EntryDao
import com.keagan.conclusion.data.local.EntryEntity
import com.keagan.conclusion.domain.model.Entry
import com.keagan.conclusion.domain.model.EntryType
import com.keagan.conclusion.domain.model.TaskStatus
import com.keagan.conclusion.domain.repo.EntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class EntryRepositoryImpl(private val dao: EntryDao) : EntryRepository {

    private val dateFmt = DateTimeFormatter.ISO_LOCAL_DATE

    private fun EntryEntity.toDomain() = Entry(
        id = id,
        userId = userId,
        type = EntryType.valueOf(type),
        title = title,
        content = content,
        date = date?.let { LocalDate.parse(it, dateFmt) },
        status = status?.let { TaskStatus.valueOf(it) },
        createdAt = Instant.parse(createdAt),
        updatedAt = Instant.parse(updatedAt)
    )

    private fun Entry.toEntity() = EntryEntity(
        id = if (id.isBlank()) UUID.randomUUID().toString() else id,
        userId = userId,
        type = type.name,
        title = title,
        content = content,
        date = date?.format(dateFmt),
        status = status?.name,
        createdAt = createdAt.toString(),
        updatedAt = Instant.now().toString()
    )

    override fun observe(type: EntryType): Flow<List<Entry>> =
        dao.observeByType(type.name).map { list -> list.map { it.toDomain() } }

    override suspend fun upsert(entry: Entry) {
        dao.upsert(entry.toEntity())
    }

    override suspend fun delete(id: String) {
        dao.find(id)?.let { dao.delete(it) }
    }

    override suspend fun find(id: String): Entry? = dao.find(id)?.toDomain()
}
