package com.flowintent.network.data

import androidx.annotation.Keep

@Keep
data class GroqResponse(
    val choices: List<GroqChoice>
)
