 /**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.data.secure

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import com.flowintent.core.db.repository.EncryptedProtoRepository
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

 internal class EncryptedProtoRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<SecurePrefs>
): EncryptedProtoRepository {

    override suspend fun updateToken(token: String) {
        dataStore.updateData { it.toBuilder().setToken(token).build() }
    }

    override fun clear(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            dataStore.updateData { SecurePrefs.getDefaultInstance() }
            emit(Resource.Success(Unit))
        } catch (e: IOException) {
            emit(Resource.Error(e.message ?: "Failed to clear data store"))
        }
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

    override suspend fun updateUid(uid: String) {
        dataStore.updateData { it.toBuilder().setUid(uid).build() }
    }

    override suspend fun saveProfileImageUrl(url: String) {
        dataStore.updateData { it.toBuilder().setProfileImageUrl(url).build() }
    }

    override fun uidFlow(): Flow<String?> = dataStore.data.map { it.uid.takeIf { u -> u.isNotEmpty() } }

    override fun nameFlow(): Flow<String?> =
        dataStore.data.map { store -> store.name.takeIf { it.isNotEmpty() } }

    override fun emailFlow(): Flow<String?> =
        dataStore.data.map { store -> store.email.takeIf { it.isNotEmpty() } }

    override fun profileImageUrlFlow(): Flow<String?> =
        dataStore.data.map { store -> store.profileImageUrl.takeIf { it.isNotEmpty() } }

    override suspend fun updateLanguage(language: String) {
        dataStore.updateData { it.toBuilder().setLanguage(language).build() }
    }

    override fun languageFlow(): Flow<String?> =
        dataStore.data.map { it.language.takeIf { it.isNotEmpty() } }

    override suspend fun updateTheme(theme: String) {
        dataStore.updateData { it.toBuilder().setTheme(theme).build() }
    }

    override fun themeFlow(): Flow<String?> =
        dataStore.data.map { it.theme.takeIf { it.isNotEmpty() } }

}
