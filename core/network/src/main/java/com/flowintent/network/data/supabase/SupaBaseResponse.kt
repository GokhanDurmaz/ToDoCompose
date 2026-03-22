package com.flowintent.network.data.supabase

data class SupaBaseResponse(
    val status: String,
    val message: String,
    val imageUrl: String? = null
)
