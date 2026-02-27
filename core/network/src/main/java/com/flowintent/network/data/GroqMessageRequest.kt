package com.flowintent.network.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GroqMessageRequest(
    @SerializedName("role")
    val role: String,
    
    @SerializedName("content")
    val content: String
)
