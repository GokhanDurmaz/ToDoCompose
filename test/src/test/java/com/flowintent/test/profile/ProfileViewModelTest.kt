package com.flowintent.test.profile

import android.content.Context
import com.flowintent.core.db.auth.ChangePasswordUseCase
import com.flowintent.core.db.auth.GetUserProfileUseCase
import com.flowintent.core.db.profile.DownloadAndSaveUseCase
import com.flowintent.core.db.profile.GetLocalAvatarUseCase
import com.flowintent.core.db.profile.ObserveUserProfileUseCase
import com.flowintent.core.db.profile.UploadProfileUseCase
import com.flowintent.core.db.model.UserProfile
import com.flowintent.core.db.repository.EncryptedProtoRepository
import com.flowintent.core.util.Resource
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.profile.ui.vm.ProfileViewModel
import com.flowintent.test.rules.MainDispatcherRule
import com.flowintent.test.scenarios.UseCaseScenarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var context: Context
    @Mock
    private lateinit var navigationDispatcher: NavigationDispatcher
    @Mock
    private lateinit var changePasswordUseCase: ChangePasswordUseCase
    @Mock
    private lateinit var getUserProfileUseCase: GetUserProfileUseCase
    @Mock
    private lateinit var uploadProfileUseCase: UploadProfileUseCase
    @Mock
    private lateinit var downloadAndSaveUseCase: DownloadAndSaveUseCase
    @Mock
    private lateinit var getLocalAvatarUseCase: GetLocalAvatarUseCase
    @Mock
    private lateinit var observeUserProfileUseCase: ObserveUserProfileUseCase
    @Mock
    private lateinit var encryptedProtoRepository: EncryptedProtoRepository

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(encryptedProtoRepository.uidFlow()).thenReturn(flowOf("uid-123"))
        whenever(observeUserProfileUseCase()).thenReturn(flowOf(Resource.Loading))
        whenever(getUserProfileUseCase()).thenReturn(flowOf(Resource.Loading))
        
        viewModel = ProfileViewModel(
            context,
            navigationDispatcher,
            changePasswordUseCase,
            getUserProfileUseCase,
            uploadProfileUseCase,
            downloadAndSaveUseCase,
            getLocalAvatarUseCase,
            observeUserProfileUseCase,
            encryptedProtoRepository
        )
    }

    @Test
    fun `observeUserProfile updates uiState with user profile and stops loading`() = runTest {
        val userProfile = UserProfile(name = "John", surname = "Doe", email = "john@example.com")
        whenever(observeUserProfileUseCase()).thenReturn(UseCaseScenarios.success(userProfile))
        whenever(getUserProfileUseCase()).thenReturn(flowOf(Resource.Loading))

        // Create a new ViewModel to trigger init block with the success flow
        viewModel = ProfileViewModel(
            context,
            navigationDispatcher,
            changePasswordUseCase,
            getUserProfileUseCase,
            uploadProfileUseCase,
            downloadAndSaveUseCase,
            getLocalAvatarUseCase,
            observeUserProfileUseCase,
            encryptedProtoRepository
        )

        assertEquals(userProfile, viewModel.uiState.value.userProfile)
        assertEquals(false, viewModel.uiState.value.isProfileLoading)
    }

    @Test
    fun `changePassword updates state to success`() = runTest {
        viewModel.onOldPasswordChange("old")
        viewModel.onNewPasswordChange("new")
        
        whenever(changePasswordUseCase("old", "new"))
            .thenReturn(UseCaseScenarios.success(Unit))

        viewModel.changePassword()

        assertEquals(Resource.Success(Unit), viewModel.uiState.value.changePasswordState)
    }
}
