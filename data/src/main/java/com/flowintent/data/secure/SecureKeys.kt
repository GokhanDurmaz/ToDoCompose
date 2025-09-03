package com.flowintent.data.secure

import android.content.Context
import android.util.Base64
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.flowintent.core.db.security.ISecurePrefsRepository
import com.google.crypto.tink.Aead
import kotlinx.coroutines.flow.first

// DataStore tanımı
val Context.securePrefs by preferencesDataStore(name = "secure_prefs")

object SecureKeys {
    val TOKEN = stringPreferencesKey("token")
    val REFRESH = stringPreferencesKey("refresh")
}

// Yardımcılar
private fun encryptToBase64(aead: Aead, plain: ByteArray, ad: ByteArray? = null): String {
    val ct = aead.encrypt(plain, ad ?: ByteArray(0))
    return Base64.encodeToString(ct, Base64.NO_WRAP)
}

private fun decryptFromBase64(aead: Aead, b64: String, ad: ByteArray? = null): ByteArray {
    val ct = Base64.decode(b64, Base64.NO_WRAP)
    return aead.decrypt(ct, ad ?: ByteArray(0))
}

// Repository
class SecurePrefsRepositoryImpl(private val context: Context): ISecurePrefsRepository {

    private val aead by lazy { CryptoProvider.aead(context) }
    private val AD_TOKEN = "token_v1".toByteArray()
    private val AD_REFRESH = "refresh_v1".toByteArray()

    override suspend fun saveToken(token: String) {
        val b64 = encryptToBase64(aead, token.encodeToByteArray(), AD_TOKEN)
        context.securePrefs.edit { it[SecureKeys.TOKEN] = b64 }
    }

    suspend fun readToken(): String? =
        context.securePrefs.data.first()[SecureKeys.TOKEN]?.let { b64 ->
            decryptFromBase64(aead, b64, AD_TOKEN).decodeToString()
        }

    suspend fun saveRefresh(refresh: String) {
        val b64 = encryptToBase64(aead, refresh.encodeToByteArray(), AD_REFRESH)
        context.securePrefs.edit { it[SecureKeys.REFRESH] = b64 }
    }

    suspend fun readRefresh(): String? =
        context.securePrefs.data.first()[SecureKeys.REFRESH]?.let { b64 ->
            decryptFromBase64(aead, b64, AD_REFRESH).decodeToString()
        }

    suspend fun clearAll() {
        context.securePrefs.edit { it.clear() }
    }
}
