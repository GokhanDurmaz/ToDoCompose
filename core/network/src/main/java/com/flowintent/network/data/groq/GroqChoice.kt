package com.flowintent.network.data.groq

import androidx.annotation.Keep

@Keep
data class GroqChoice(
    val message: GroqMessageResponse
)
