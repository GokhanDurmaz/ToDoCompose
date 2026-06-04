 /**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.repository

import kotlinx.coroutines.flow.Flow

interface EncryptedProtoRepository {

    suspend fun updateToken(token: String)

    suspend fun clear()

    suspend fun saveUserInfo(name: String, email: String)

    suspend fun updateUid(uid: String)

    suspend fun saveProfileImageUrl(url: String)

    fun uidFlow(): Flow<String?>

    fun tokenFlow(): Flow<String?>

    fun nameFlow(): Flow<String?>

    fun emailFlow(): Flow<String?>

    fun profileImageUrlFlow(): Flow<String?>

    suspend fun updateLanguage(language: String)

    fun languageFlow(): Flow<String?>

    suspend fun updateTheme(theme: String)

    fun themeFlow(): Flow<String?>
}
