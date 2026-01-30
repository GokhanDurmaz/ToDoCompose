package com.flowintent.data.secure

import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import java.io.InputStream
import java.io.OutputStream

internal class EncryptedSecurePrefsSerializer(
    private val aead: Aead
) : Serializer<SecurePrefs> {

    override val defaultValue: SecurePrefs = SecurePrefs.getDefaultInstance()

    private val adfile = "secure_prefs_proto_v1".toByteArray()

    override suspend fun readFrom(input: InputStream): SecurePrefs {
        return try {
            val bytes = input.readBytes()
            if (bytes.isEmpty()) return defaultValue

            val plain = aead.decrypt(bytes, adfile)
            SecurePrefs.parseFrom(plain)
        } catch (_: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: SecurePrefs, output: OutputStream) {
        val plain = t.toByteArray()
        val ct = aead.encrypt(plain, adfile)
        output.write(ct)
        output.flush()
    }
}
