package com.keagan.conclusion.data.remote

import retrofit2.Response
import retrofit2.http.*

interface PlannerApi {
    // Health / Quote
    @GET("health")
    suspend fun health(): Map<String, Any>

    @GET("quote/today")
    suspend fun getQuote(): QuoteDto

    // Tasks
    @GET("tasks")
    suspend fun getTasks(@Query("userId") userId: String): List<TaskDto>

    @POST("tasks")
    suspend fun createTask(@Body body: TaskDto): Response<TaskDto>

    @PATCH("tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: String,
        @Body body: Map<String, Any>
    ): Response<TaskDto>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<Unit>
}
