package com.keagan.conclusion.data.remote

import com.keagan.conclusion.domain.model.Task

/** Server DTOs */
data class QuoteDto(val text: String)

/** Mongo returns _id on GET; we omit it on POST (default = null) */
data class TaskDto(
    val _id: String? = null,
    val userId: String,
    val title: String,
    val status: String            // "TODO" | "DOING" | "DONE"
)

/** Map domain Task -> POST payload */
fun Task.toDto() = TaskDto(
    userId = userId,
    title = title,
    status = status.name
)
