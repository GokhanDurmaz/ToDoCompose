/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.network.di

import android.util.Log
import com.flowintent.core.util.AppEventTracker
import com.flowintent.network.network.interceptors.NetworkErrorInterceptor
import com.flowintent.network.network.services.GroqApiService
import com.flowintent.network.util.NativeConfig
import com.flowintent.network.util.NetworkUtil
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TAG = "NetworkModule"
    private const val FALLBACK_URL = "https://invalid.config/"

    @Singleton
    @Provides
    fun provideOkHttpClient(
        networkErrorInterceptor: NetworkErrorInterceptor
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(networkErrorInterceptor)
            .callTimeout(NetworkUtil.CALL_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(NetworkUtil.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
        eventTracker: AppEventTracker
    ): Retrofit {
        val builder = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))

        return try {
            val baseUrl = NativeConfig.getBaseUrl()
            builder.baseUrl(baseUrl).build()
        } catch (e: Exception) {
            Log.e(TAG, "Invalid Base URL provided: ${e.message}. Using fallback.")
            eventTracker.logException(e)
            eventTracker.logMessage("Invalid Base URL provided: ${e.message}")
            builder.baseUrl(FALLBACK_URL).build()
        }
    }

    @Provides
    @Singleton
    fun provideGroqApiService(retrofit: Retrofit): GroqApiService {
        return retrofit.create(GroqApiService::class.java)
    }
}
