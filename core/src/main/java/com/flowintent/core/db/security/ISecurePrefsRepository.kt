package com.flowintent.core.db.security

interface ISecurePrefsRepository {
    suspend fun saveToken(token: String)
}
