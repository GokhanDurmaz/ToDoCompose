package com.flowintent.network.util

object NativeConfig {
    init {
        System.loadLibrary("flowintent_crypto")
    }

    external fun getGroqApiKey(): String
    external fun getBaseUrl(): String
}
