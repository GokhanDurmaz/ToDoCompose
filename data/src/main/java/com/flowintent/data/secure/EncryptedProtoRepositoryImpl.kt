package com.flowintent.data.secure

import kotlinx.coroutines.flow.Flow
import androidx.datastore.core.DataStore
import com.flowintent.core.db.repository.EncryptedProtoRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class EncryptedProtoRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<SecurePrefs>
): EncryptedProtoRepository {

    override suspend fun updateToken(token: String) {
        dataStore.updateData { it.toBuilder().setToken(token).build() }
    }

    override suspend fun clear() {
        dataStore.updateData { SecurePrefs.getDefaultInstance() }
    }

    override fun tokenFlow(): Flow<String?> =
        dataStore.data.map { it.token.takeIf { t -> t.isNotEmpty() } }

    override suspend fun saveUserInfo(name: String, email: String) {
        dataStore.updateData { currentPrefs ->
            currentPrefs.toBuilder()
                .setName(name)
                .setEmail(email)
                .build()
        }
    }

    override fun nameFlow(): Flow<String?> =
        dataStore.data.map { store -> store.name.takeIf { it.isNotEmpty() } }

    override fun emailFlow(): Flow<String?> =
        dataStore.data.map { store -> store.email.takeIf { it.isNotEmpty() } }

}
