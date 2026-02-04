package com.flowintent.core.db.auth

import com.flowintent.core.db.UserProfile
import com.flowintent.core.db.repository.AuthRepository
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow

class SignUpUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(name: String, surname: String, email: String, password: String): Flow<Resource<Unit>> =
        authRepository.registerUser(name, surname, email, password)
}

class SignInUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(email: String, password: String): Flow<Resource<String>> =
        authRepository.loginUser(email, password)
}

class GetUserProfileUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<Resource<UserProfile>> = authRepository.getUserProfile()
}

class ForgetPasswordUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(email: String): Flow<Resource<Unit>> = authRepository.forgetPassword(email)
}
