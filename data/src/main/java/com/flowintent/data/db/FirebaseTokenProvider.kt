package com.flowintent.data.db

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseTokenProvider @Inject constructor(
    private val auth: FirebaseAuth
) {
    suspend fun getFreshToken(): String? = withContext(Dispatchers.IO) {
        try {
            val result = auth.currentUser?.getIdToken(false)?.await()
            result?.token
        } catch (e: Exception) {
            null
        }
    }
}
