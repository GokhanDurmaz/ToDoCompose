package com.flowintent.data.db.repository

import android.net.Uri
import com.flowintent.core.db.model.UserProfile
import com.flowintent.core.db.repository.AuthRepository
import com.flowintent.core.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
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

    override fun getUserProfile(): Flow<Resource<UserProfile>> = flow {
        emit(Resource.Loading)
        try {
            val userId = auth.currentUser?.uid ?: throw Exception("Failed to login credential")

            val document = db.collection("users").document(userId).get().await()

            val userProfile = document.toObject(UserProfile::class.java)
            if (userProfile != null) {
                emit(Resource.Success(userProfile))
            } else {
                emit(Resource.Error("Failed to get user info"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }

    override fun loginUser(
        email: String,
        password: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading)
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw Exception("Could not found user")

            val tokenResult = user.getIdToken(false).await()
            val token = tokenResult.token ?: throw Exception("Could not get token")

            emit(Resource.Success(token))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun forgetPassword(email: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            auth.sendPasswordResetEmail(email).await()

            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Could not send email to reset password."))
        }
    }

    override fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val user = auth.currentUser ?: throw Exception("User not logged in")
            val email = user.email ?: throw Exception("User email not found")

            val credential = com.google.firebase.auth.EmailAuthProvider.getCredential(email, currentPassword)
            user.reauthenticate(credential).await()

            user.updatePassword(newPassword).await()

            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to change password"))
        }
    }

    override fun uploadProfileImage(imageUri: Uri): Flow<Resource<String>> = flow {
        emit(Resource.Loading)
        try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")

            val storageRef = storage.reference.child("profile_images/$userId.jpg")

            storageRef.putFile(imageUri).await()

            val downloadUrl = storageRef.downloadUrl.await().toString()

            db.collection("users")
                .document(userId)
                .update("profileImageUrl", downloadUrl)
                .await()

            emit(Resource.Success(downloadUrl))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Image upload failed"))
        }
    }

    override fun observeUserProfile(): Flow<Resource<UserProfile>> = callbackFlow {
        trySend(Resource.Loading)

        val userId = auth.currentUser?.uid
        if (userId == null) {
            trySend(Resource.Error("User not authenticated"))
            close()
            return@callbackFlow
        }

        val documentRef = db.collection("users").document(userId)

        val registration = documentRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Resource.Error(error.message ?: "An error occurred"))
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val userProfile = snapshot.toObject(UserProfile::class.java)
                if (userProfile != null) {
                    trySend(Resource.Success(userProfile))
                } else {
                    trySend(Resource.Error("User profile mapping failed"))
                }
            }
        }

        awaitClose {
            registration.remove()
        }
    }
}
