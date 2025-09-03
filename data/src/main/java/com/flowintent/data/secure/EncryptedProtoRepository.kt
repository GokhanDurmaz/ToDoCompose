package com.flowintent.data.secure

import kotlinx.coroutines.flow.Flow
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedProtoRepository @Inject constructor(
    private val dataStore: DataStore<SecurePrefs>
) {

    val secureFlow: Flow<SecurePrefs> = dataStore.data

    suspend fun updateToken(token: String) {
        dataStore.updateData { it.toBuilder().setToken(token).build() }
    }

    suspend fun clear() {
        dataStore.updateData { SecurePrefs.getDefaultInstance() }
    }

    fun tokenFlow(): Flow<String?> =
        dataStore.data.map { it.token.takeIf { t -> t.isNotEmpty() } }
}

