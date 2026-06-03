/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.repository

import android.graphics.Bitmap
import com.flowintent.core.db.model.UserProfile
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface SupaBaseRepository {
    fun uploadProfileImage(imageBytes: ByteArray, onUpdateAuth: (String) -> Flow<Resource<Unit>>): Flow<Resource<String>>

    fun observeUserProfile(): Flow<Resource<UserProfile>>

    suspend fun downloadAndSave(userId: String): Bitmap?

    suspend fun getLocalAvatar(userId: String): Bitmap?

    fun clearLocalAvatar(userId: String)
}
