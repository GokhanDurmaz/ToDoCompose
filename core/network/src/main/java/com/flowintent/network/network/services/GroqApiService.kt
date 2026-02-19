package com.flowintent.network.network.services

import com.flowintent.network.data.GroqRequest
import com.flowintent.network.data.GroqResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GroqApiService {
    @POST("openai/v1/chat/completions")
    suspend fun getCompletion(
        @Header("Authorization") auth: String,
        @Body request: GroqRequest
    ): GroqResponse
}
