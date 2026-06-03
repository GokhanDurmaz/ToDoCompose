/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.data.secure

import android.content.Context
import android.util.Log
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import java.security.KeyStore

internal object CryptoProvider {
    private const val TAG = "CryptoProvider"
    private const val MASTER_KEY_URI = "android-keystore://tink_master_key"
    private const val KEYSET_NAME = "tink_keyset"
    private const val PREF_FILE = "tink_prefs"

    fun aead(context: Context): Aead {
        return try {
            initAead(context)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Aead, attempting reset", e)
            // If initialization fails, the key might be corrupted or missing.
            // Resetting the keyset and master key. Note: This makes previous data unreadable.
            context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE).edit().clear().apply()
            try {
                val keyStore = KeyStore.getInstance("AndroidKeyStore")
                keyStore.load(null)
                keyStore.deleteEntry("tink_master_key")
            } catch (keystoreException: Exception) {
                Log.e(TAG, "Failed to clear Keystore", keystoreException)
            }
            initAead(context)
        }
    }

    private fun initAead(context: Context): Aead {
        val handle = AndroidKeysetManager.Builder()
            .withSharedPref(context, KEYSET_NAME, PREF_FILE)
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
        return handle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)
    }
}
