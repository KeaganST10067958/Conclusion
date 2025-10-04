package com.keagan.conclusion.data.remote.dto

data class QuoteDto(val text: String)

data class TaskDto(
    val _id: String? = null,
    val userId: String,
    val title: String,
    val status: String = "TODO" // TODO / DOING / DONE
)
