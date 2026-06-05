/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.profile

import android.graphics.Bitmap
import com.flowintent.core.db.model.UserProfile
import com.flowintent.core.db.repository.AuthRepository
import com.flowintent.core.db.repository.SupaBaseRepository
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadProfileUseCase @Inject constructor(
    private val supaBaseRepository: SupaBaseRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(imageBytes: ByteArray): Flow<Resource<String>> =
        supaBaseRepository.uploadProfileImage(imageBytes) { imageUrl ->
            authRepository.updateProfileImageUrl(imageUrl)
        }
}

class DownloadAndSaveUseCase @Inject constructor(private val supaBaseRepository: SupaBaseRepository) {
    suspend operator fun invoke(userId: String): Bitmap? = supaBaseRepository.downloadAndSave(userId)
}

class GetLocalAvatarUseCase @Inject constructor(private val supaBaseRepository: SupaBaseRepository) {
    suspend operator fun invoke(userId: String): Bitmap? = supaBaseRepository.getLocalAvatar(userId)
}

class ObserveUserProfileUseCase @Inject constructor(private val supaBaseRepository: SupaBaseRepository) {
    operator fun invoke(): Flow<Resource<UserProfile>> = supaBaseRepository.observeUserProfile()
}
