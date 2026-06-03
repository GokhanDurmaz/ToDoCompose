/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.network.network.services

import com.flowintent.network.data.groq.GroqRequest
import com.flowintent.network.data.groq.GroqResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Interface defining the Groq API service for chat completions.
 */
interface GroqApiService {
    @POST("openai/v1/chat/completions")
    suspend fun getCompletion(
        @Header("Authorization") auth: String,
        @Body request: GroqRequest
    ): GroqResponse
}
