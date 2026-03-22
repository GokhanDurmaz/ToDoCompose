package com.flowintent.data.db.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.flowintent.core.db.model.UserProfile
import com.flowintent.core.db.repository.SupaBaseRepository
import com.flowintent.core.util.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class SupaBaseRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val auth: FirebaseAuth,
    private val supabase: SupabaseClient,
    private val postgrest: Postgrest,
): SupaBaseRepository {

    private suspend fun uploadAvatar(userId: String, imageBytes: ByteArray): String? {
        return try {
            val fileName = "avatar_$userId.jpg"
            val bucket = supabase.storage.from("avatars")

            bucket.upload(
                path = fileName,
                data = imageBytes
            ) {
                upsert = true
                contentType = ContentType.parse("image/jpeg")
            }

            val url = bucket.publicUrl(fileName)
            url
        } catch (e: Exception) {
            Log.e("SupaBaseRepositoryImpl",e.message.toString())
            null
        }
    }

    override suspend fun downloadAndSave(userId: String): Bitmap? = withContext(Dispatchers.IO) {
        val fileName = "avatar_$userId.jpg"
        val localFile = File(context.filesDir, "avatars/$fileName")

        try {
            if (localFile.exists()) {
                Log.d("BITMAP_DEBUG", "File path: ${localFile.absolutePath}")
                Log.d("BITMAP_DEBUG", "File length: ${localFile.length()} bytes")
                Log.d("BITMAP_DEBUG", "isReadable: ${localFile.canRead()}")

                if (localFile.length() == 0L) {
                    Log.e("BITMAP_DEBUG", "Err: File is empty(0 byte)! Failed to download the file.")
                    localFile.delete()
                    return@withContext null
                }

                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)

                if (bitmap == null) {
                    Log.e("BITMAP_DEBUG", "Err: BitmapFactory failed to decode file. File format might be disrupted.")
                } else {
                    Log.d("BITMAP_DEBUG", "Successful: Bitmap created. Width: ${bitmap.width}")
                }
                return@withContext bitmap
            }

            val bucket = supabase.storage.from("avatars")
            val bytes = bucket.downloadPublic(fileName)

            localFile.parentFile?.mkdirs()
            localFile.writeBytes(bytes)

            BitmapFactory.decodeFile(localFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun uploadProfileImage(imageBytes: ByteArray): Flow<Resource<String>> = flow {
        emit(Resource.Loading)
        try {
            val userId = auth.currentUser?.uid ?: throw Exception("Auth user not found")

            val downloadUrl = uploadAvatar(userId, imageBytes)
                ?: throw Exception("Storage upload failed")

            postgrest.from("users").update(
                {
                    set("profileImageUrl", downloadUrl)
                }
            ) {
                filter {
                    eq("uid", userId)
                }
            }

            clearLocalAvatar(userId)

            emit(Resource.Success(downloadUrl))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Update failed"))
        }
    }

    @OptIn(SupabaseExperimental::class)
    override fun observeUserProfile(): Flow<Resource<UserProfile>> = callbackFlow {
        trySend(Resource.Loading)

        val userId = auth.currentUser?.uid
        if (userId == null) {
            trySend(Resource.Error("Couldn't find any user."))
            close()
            return@callbackFlow
        }

        val job = launch {
            postgrest.from("users")
                .selectAsFlow(
                    primaryKey = UserProfile::uid,
                    filter = FilterOperation("uid", FilterOperator.EQ, userId)
                ).collect { list ->
                    if (list.isNotEmpty()) {
                        trySend(Resource.Success(list.first()))
                    }
                }
        }
        awaitClose { job.cancel() }
    }

    override fun clearLocalAvatar(userId: String) {
        val file = File(context.filesDir, "private_avatars/avatar_$userId.jpg")
        if (file.exists()) file.delete()
    }
}
