package com.flowintent.core.db.auth

import com.flowintent.core.db.repository.AuthRepository
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow

class AuthUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(name: String, surname: String, email: String, password: String): Flow<Resource<Unit>> =
        authRepository.registerUser(name, surname, email, password)
}
