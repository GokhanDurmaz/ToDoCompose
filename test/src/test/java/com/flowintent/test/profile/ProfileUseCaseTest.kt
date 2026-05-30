package com.flowintent.test.profile

import android.graphics.Bitmap
import com.flowintent.core.db.model.UserProfile
import com.flowintent.core.db.profile.DownloadAndSaveUseCase
import com.flowintent.core.db.profile.ObserveUserProfileUseCase
import com.flowintent.core.db.profile.UploadProfileUseCase
import com.flowintent.core.db.repository.SupaBaseRepository
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class ProfileUseCaseTest {

    @Mock
    private lateinit var supaBaseRepository: SupaBaseRepository

    @Mock
    private lateinit var mockBitmap: Bitmap

    private lateinit var uploadProfileUseCase: UploadProfileUseCase
    private lateinit var downloadAndSaveUseCase: DownloadAndSaveUseCase
    private lateinit var observeUserProfileUseCase: ObserveUserProfileUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        uploadProfileUseCase = UploadProfileUseCase(supaBaseRepository)
        downloadAndSaveUseCase = DownloadAndSaveUseCase(supaBaseRepository)
        observeUserProfileUseCase = ObserveUserProfileUseCase(supaBaseRepository)
    }

    @Test
    fun `UploadProfileUseCase returns image url when repository succeeds`() = runTest {
        val bytes = byteArrayOf(1, 2, 3)
        val expected = Resource.Success("http://image.url")
        whenever(supaBaseRepository.uploadProfileImage(bytes))
            .thenReturn(flowOf(expected))

        uploadProfileUseCase(bytes).collect { result ->
            assertEquals(expected, result)
        }
    }

    @Test
    fun `DownloadAndSaveUseCase returns bitmap when repository succeeds`() = runTest {
        val userId = "user123"
        whenever(supaBaseRepository.downloadAndSave(userId))
            .thenReturn(mockBitmap)

        val result = downloadAndSaveUseCase(userId)
        assertEquals(mockBitmap, result)
    }

    @Test
    fun `ObserveUserProfileUseCase returns user profile when repository succeeds`() = runTest {
        val profile = UserProfile(name = "Jane", surname = "Doe", email = "jane@example.com")
        val expected = Resource.Success(profile)
        whenever(supaBaseRepository.observeUserProfile())
            .thenReturn(flowOf(expected))

        observeUserProfileUseCase().collect { result ->
            assertEquals(expected, result)
        }
    }
}
