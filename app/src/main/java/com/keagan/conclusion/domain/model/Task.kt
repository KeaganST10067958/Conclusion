package com.keagan.conclusion.domain.model

import java.time.Instant

data class Task(
    val id: String,
    val userId: String,
    val title: String,
    val status: TaskStatus,
    val createdAt: Instant,
    val updatedAt: Instant
)
