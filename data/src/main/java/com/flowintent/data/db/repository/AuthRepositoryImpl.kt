package com.flowintent.data.db.repository

import com.flowintent.core.db.UserProfile
import com.flowintent.core.db.repository.AuthRepository
import com.flowintent.core.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
): AuthRepository {

    override fun registerUser(name: String, surname: String, email: String, password: String): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading)
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid ?: throw Exception("Failed to get userId")

                val user = UserProfile(uid = userId, name = name, surname = surname, email = email)
                db.collection("users").document(userId).set(user).await()

                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
}
