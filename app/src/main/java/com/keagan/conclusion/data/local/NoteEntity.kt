package com.keagan.conclusion.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userId: String = "local",           // replace with signed-in account id later
    val title: String,
    val content: String?,
    val createdAt: String = Instant.now().toString(),
    val updatedAt: String = Instant.now().toString()
)
