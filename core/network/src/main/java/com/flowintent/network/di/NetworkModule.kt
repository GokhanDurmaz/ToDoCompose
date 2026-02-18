package com.flowintent.network.di

import com.flowintent.network.BuildConfig
import com.flowintent.network.util.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        sslSocketFactory: SSLSocketFactory,
        trustManager: X509TrustManager
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(NetworkUtil.CALL_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(NetworkUtil.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .sslSocketFactory(sslSocketFactory, trustManager)
            .build()
        return okHttpClient
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .build()
}
