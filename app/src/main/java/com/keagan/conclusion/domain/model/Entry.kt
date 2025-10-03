package com.keagan.conclusion.domain.model

import java.time.Instant
import java.time.LocalDate

enum class EntryType { NOTE, TASK, EVENT }

data class Entry(
    val id: String,
    val userId: String,
    val type: EntryType,
    val title: String,
    val content: String? = null,
    val date: LocalDate? = null,
    val status: TaskStatus? = null,
    val createdAt: Instant,
    val updatedAt: Instant
)
