package com.keagan.conclusion.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class EntryEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val type: String,      // NOTE | TASK | EVENT
    val title: String,
    val content: String?,
    val date: String?,     // YYYY-MM-DD
    val status: String?,   // TODO | DOING | DONE
    val createdAt: String, // ISO_INSTANT
    val updatedAt: String  // ISO_INSTANT
)
