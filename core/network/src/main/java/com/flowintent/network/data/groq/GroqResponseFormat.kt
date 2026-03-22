package com.flowintent.network.data.groq

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GroqResponseFormat(
    @SerializedName("type")
    val type: String
)
