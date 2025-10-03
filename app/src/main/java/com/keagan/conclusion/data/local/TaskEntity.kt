package com.keagan.conclusion.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userId: String = "local",
    val title: String,
    val status: String,                     // "TODO" | "DOING" | "DONE"
    val createdAt: String = Instant.now().toString(),
    val updatedAt: String = Instant.now().toString()
)
