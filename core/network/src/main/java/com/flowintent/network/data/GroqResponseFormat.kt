package com.flowintent.network.data

import com.google.gson.annotations.SerializedName

data class GroqResponseFormat(
    @SerializedName("type")
    val type: String
)
