package com.keagan.conclusion.data.remote

import retrofit2.http.*

data class QuoteDto(val text: String)

// ---- Tasks DTOs ----
data class TaskDto(
    val _id: String,      // Mongo id
    val userId: String,
    val title: String,
    val status: String    // "TODO" | "DOING" | "DONE"
)

data class CreateTaskRequest(
    val userId: String,
    val title: String,
    val status: String = "TODO"
)

data class UpdateTaskRequest(
    val title: String? = null,
    val status: String? = null
)

interface PlannerApi {
    @GET("quote/today")
    suspend fun quoteOfDay(): QuoteDto

    // ---- Tasks ----
    @GET("tasks")
    suspend fun listTasks(@Query("userId") userId: String): List<TaskDto>

    @POST("tasks")
    suspend fun createTask(@Body body: CreateTaskRequest): TaskDto

    @PATCH("tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body body: UpdateTaskRequest): TaskDto

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String)
}
