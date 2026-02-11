package com.flowintent.core.db.repository

import kotlinx.coroutines.flow.Flow

interface EncryptedProtoRepository {

    suspend fun updateToken(token: String)

    suspend fun clear()

    suspend fun saveUserInfo(name: String, email: String)

    fun tokenFlow(): Flow<String?>

    fun nameFlow(): Flow<String?>

    fun emailFlow(): Flow<String?>
}
