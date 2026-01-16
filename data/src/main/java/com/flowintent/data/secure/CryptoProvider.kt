package com.flowintent.data.secure

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.integration.android.AndroidKeysetManager

internal object CryptoProvider {
    private const val MASTER_KEY_URI = "android-keystore://tink_master_key"
    private const val KEYSET_NAME = "tink_keyset"
    private const val PREF_FILE = "tink_prefs"

    fun aead(context: Context): Aead {
        val handle = AndroidKeysetManager.Builder()
            .withSharedPref(context, KEYSET_NAME, PREF_FILE)
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
        return handle.getPrimitive(RegistryConfiguration.get(),Aead::class.java)
    }
}