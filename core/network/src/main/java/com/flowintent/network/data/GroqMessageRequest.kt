package com.flowintent.network.data

import com.google.gson.annotations.SerializedName

data class GroqMessageRequest(
    @SerializedName("role")
    val role: String,
    
    @SerializedName("content")
    val content: String
)
