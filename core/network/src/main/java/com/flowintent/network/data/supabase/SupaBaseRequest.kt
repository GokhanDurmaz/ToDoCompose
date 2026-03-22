package com.flowintent.network.data.supabase

data class SupaBaseRequest(
    val userId: String,
    val action: String,
    val metadata: Map<String, String>? = null
)
