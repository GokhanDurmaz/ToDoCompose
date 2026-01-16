package com.flowintent.core.db.security

interface SecurePrefsRepository {
    suspend fun saveToken(token: String)
}
