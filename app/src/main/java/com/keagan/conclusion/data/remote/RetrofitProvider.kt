package com.keagan.conclusion.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {
    fun api(baseUrl: String): PlannerApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl.ensureTrailingSlash())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(PlannerApi::class.java)
    }

    private fun String.ensureTrailingSlash(): String =
        if (endsWith("/")) this else "$this/"
}
