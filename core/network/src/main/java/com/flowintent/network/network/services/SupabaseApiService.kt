package com.flowintent.network.network.services

import com.flowintent.network.data.supabase.SupaBaseResponse
import com.flowintent.network.data.supabase.SupaBaseRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface SupabaseApiService {
    @POST("storage/v1/object/authenticated/avatars/{fileName}")
    suspend fun getAvatar(
        @Header("Authorization") auth: String,
        @Header("apikey") apiKey: String,
        @Path("fileName") fileName: String,
        @Body request: SupaBaseRequest
    ): SupaBaseResponse

    @GET
    suspend fun downloadFileWithDynamicUrl(
        @Url fileUrl: String,
        @Header("Authorization") auth: String,
        @Header("apikey") apiKey: String
    ): ByteArray
}
