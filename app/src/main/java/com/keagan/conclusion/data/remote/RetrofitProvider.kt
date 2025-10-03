package com.keagan.conclusion.data.remote

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitProvider {
    fun api(baseUrl: String): PlannerApi {
        val moshi = Moshi.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl) // must end with '/'
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(PlannerApi::class.java)
    }
}
