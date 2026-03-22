package com.flowintent.core.db.repository

import android.graphics.Bitmap
import com.flowintent.core.db.model.UserProfile
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface SupaBaseRepository {
    fun uploadProfileImage(imageBytes: ByteArray): Flow<Resource<String>>

    fun observeUserProfile(): Flow<Resource<UserProfile>>

    suspend fun downloadAndSave(userId: String): Bitmap?

    fun clearLocalAvatar(userId: String)
}
