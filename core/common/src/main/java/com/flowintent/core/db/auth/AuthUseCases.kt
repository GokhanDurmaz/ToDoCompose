/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.auth

import com.flowintent.core.db.model.UserProfile
import com.flowintent.core.db.repository.AuthRepository
import com.flowintent.core.db.repository.EncryptedProtoRepository
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(name: String, surname: String, email: String, password: String): Flow<Resource<Unit>> =
        authRepository.registerUser(name, surname, email, password)
}

class SignInUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(email: String, password: String): Flow<Resource<String>> =
        authRepository.loginUser(email, password)
}

class GetUserProfileUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<Resource<UserProfile>> = authRepository.getUserProfile()
}

class ForgetPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(email: String): Flow<Resource<Unit>> = authRepository.forgetPassword(email)
}

class ChangePasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(currentPassword: String, newPassword: String): Flow<Resource<Unit>> =
        authRepository.changePassword(currentPassword, newPassword)
}

class GetTokenUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    operator fun invoke(): Flow<String?> = repository.tokenFlow()
}

class UpdateTokenUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    suspend operator fun invoke(token: String) = repository.updateToken(token)
}

class ClearEncryptedStorageUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    suspend operator fun invoke() = repository.clear()
}

class GetNameUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    operator fun invoke(): Flow<String?> = repository.nameFlow()
}

class GetEmailUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    operator fun invoke(): Flow<String?> = repository.emailFlow()
}

class GetProfileImageUrlUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    operator fun invoke(): Flow<String?> = repository.profileImageUrlFlow()
}

class GetUidUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    operator fun invoke(): Flow<String?> = repository.uidFlow()
}

class UpdateUidUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    suspend operator fun invoke(uid: String) = repository.updateUid(uid)
}

class SaveUserInfoUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    suspend operator fun invoke(name: String, email: String) = repository.saveUserInfo(name, email)
}

class SaveProfileImageUrlUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    suspend operator fun invoke(url: String) = repository.saveProfileImageUrl(url)
}
