package com.flowintent.core.db.repository

import kotlinx.coroutines.flow.Flow

interface EncryptedProtoRepository {

    suspend fun updateToken(token: String)

    suspend fun clear()

    fun tokenFlow(): Flow<String?>
}
