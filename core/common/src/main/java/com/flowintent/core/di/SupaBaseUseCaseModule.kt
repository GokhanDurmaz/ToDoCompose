package com.flowintent.core.di

import com.flowintent.core.db.profile.DownloadAndSaveUseCase
import com.flowintent.core.db.profile.GetLocalAvatarUseCase
import com.flowintent.core.db.profile.ObserveUserProfileUseCase
import com.flowintent.core.db.profile.UploadProfileUseCase
import com.flowintent.core.db.repository.AuthRepository
import com.flowintent.core.db.repository.SupaBaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupaBaseUseCaseModule {
    @Singleton
    @Provides
    fun providesUploadProfile(supaBaseRepository: SupaBaseRepository, authRepository: AuthRepository) =
        UploadProfileUseCase(supaBaseRepository, authRepository)

    @Singleton
    @Provides
    fun providesObserveUserProfile(supaBaseRepository: SupaBaseRepository) = ObserveUserProfileUseCase(supaBaseRepository)

    @Singleton
    @Provides
    fun providesDownloadAndSaveUseCase(supaBaseRepository: SupaBaseRepository) =
        DownloadAndSaveUseCase(supaBaseRepository)

    @Singleton
    @Provides
    fun providesGetLocalAvatarUseCase(supaBaseRepository: SupaBaseRepository) =
        GetLocalAvatarUseCase(supaBaseRepository)
}
