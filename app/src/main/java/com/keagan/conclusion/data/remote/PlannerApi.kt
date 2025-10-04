package com.keagan.conclusion.data.remote

import retrofit2.http.GET

data class QuoteDto(val text: String)

interface PlannerApi {
    @GET("quote/today")
    suspend fun getQuoteToday(): QuoteDto
}
