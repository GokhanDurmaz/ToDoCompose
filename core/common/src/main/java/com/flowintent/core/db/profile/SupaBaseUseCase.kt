package com.flowintent.core.db.profile

import android.graphics.Bitmap
import com.flowintent.core.db.model.UserProfile
import com.flowintent.core.db.repository.SupaBaseRepository
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow

class UploadProfileUseCase(private val supaBaseRepository: SupaBaseRepository) {
    operator fun invoke(imageBytes: ByteArray): Flow<Resource<String>> =
        supaBaseRepository.uploadProfileImage(imageBytes)
}

class DownloadAndSaveUseCase(val supaBaseRepository: SupaBaseRepository) {
    suspend operator fun invoke(userId: String): Bitmap? = supaBaseRepository.downloadAndSave(userId)
}

class ObserveUserProfileUseCase(val supaBaseRepository: SupaBaseRepository) {
    operator fun invoke(): Flow<Resource<UserProfile>> = supaBaseRepository.observeUserProfile()
}
