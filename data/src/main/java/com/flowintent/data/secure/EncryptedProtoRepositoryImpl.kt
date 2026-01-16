package com.flowintent.data.secure

import kotlinx.coroutines.flow.Flow
import androidx.datastore.core.DataStore
import com.flowintent.core.db.repository.EncryptedProtoRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class EncryptedProtoRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<SecurePrefs>
): EncryptedProtoRepository {

    val secureFlow: Flow<SecurePrefs> = dataStore.data

    override suspend fun updateToken(token: String) {
        dataStore.updateData { it.toBuilder().setToken(token).build() }
    }

    override suspend fun clear() {
        dataStore.updateData { SecurePrefs.getDefaultInstance() }
    }

    override fun tokenFlow(): Flow<String?> =
        dataStore.data.map { it.token.takeIf { t -> t.isNotEmpty() } }
}
