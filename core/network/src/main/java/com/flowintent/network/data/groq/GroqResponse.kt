package com.flowintent.network.data.groq

import androidx.annotation.Keep

@Keep
data class GroqResponse(
    val choices: List<GroqChoice>
)
