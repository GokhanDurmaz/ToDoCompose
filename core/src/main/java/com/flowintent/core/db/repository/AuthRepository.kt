package com.flowintent.core.db.repository

import com.flowintent.core.db.UserProfile
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun registerUser(name: String, surname: String, email: String, password: String): Flow<Resource<Unit>>

    fun loginUser(email: String, password: String): Flow<Resource<String>>

    fun getUserProfile(): Flow<Resource<UserProfile>>

    fun forgetPassword(email: String): Flow<Resource<Unit>>
}
