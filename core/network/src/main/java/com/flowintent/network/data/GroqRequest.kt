package com.flowintent.network.data

import com.google.gson.annotations.SerializedName

data class GroqRequest(
    @SerializedName("model")
    val model: String = "llama-3.3-70b-versatile",
    
    @SerializedName("messages")
    val messages: List<GroqMessageRequest>,
    
    @SerializedName("temperature")
    val temperature: Double = 0.1,

    @SerializedName("stream")
    val stream: Boolean = false,
    
    @SerializedName("response_format")
    val responseFormat: GroqResponseFormat = GroqResponseFormat("json_object")
)
