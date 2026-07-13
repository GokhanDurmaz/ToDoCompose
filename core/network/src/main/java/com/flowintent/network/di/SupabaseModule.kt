/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.network.di

import android.util.Log
import com.flowintent.core.util.AppEventTracker
import com.flowintent.network.util.NativeConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    private const val TAG = "SupabaseModule"
    private const val FALLBACK_URL = "https://invalid.config/"

    @Provides
    @Singleton
    fun provideSupabaseAuth(client: SupabaseClient): Auth = client.auth

    @Provides
    @Singleton
    fun providePostgrest(client: SupabaseClient): Postgrest = client.postgrest

    @Provides
    @Singleton
    fun provideSupabaseClient(eventTracker: AppEventTracker): SupabaseClient {
        return try {
            createSupabaseClient(
                supabaseUrl = NativeConfig.getSupaBaseUrl(),
                supabaseKey = NativeConfig.getSupaBaseApiKey()
            ) {
                install(Auth)
                install(Realtime)
                install(Postgrest)
                install(Storage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Invalid Supabase URL provided: ${e.message}. Using fallback.")
            eventTracker.logException(e)
            eventTracker.logMessage("Invalid Base URL provided: ${e.message}")
            createSupabaseClient(
                supabaseUrl = FALLBACK_URL,
                supabaseKey = "invalid_key"
            ) {
                install(Auth)
                install(Realtime)
                install(Postgrest)
                install(Storage)
            }
        }
    }
}
