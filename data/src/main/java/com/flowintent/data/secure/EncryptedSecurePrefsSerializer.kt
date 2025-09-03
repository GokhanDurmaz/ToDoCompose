package com.flowintent.data.secure

import android.content.Context
import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream

class EncryptedSecurePrefsSerializer(
    private val context: Context
) : Serializer<SecurePrefs> {

    override val defaultValue: SecurePrefs = SecurePrefs.getDefaultInstance()

    private val aead by lazy { CryptoProvider.aead(context) }
    private val AD_FILE = "secure_prefs_proto_v1".toByteArray()

    override suspend fun readFrom(input: InputStream): SecurePrefs {
        val bytes = input.readBytes()
        if (bytes.isEmpty()) return defaultValue

        val plain = aead.decrypt(bytes, AD_FILE)
        return SecurePrefs.parseFrom(plain)
    }

    override suspend fun writeTo(t: SecurePrefs, output: OutputStream) {
        val plain = t.toByteArray()
        val ct = aead.encrypt(plain, AD_FILE)
        output.write(ct)
        output.flush()
    }
}
