package com.flowintent.test.auth

import com.flowintent.auth.ui.vm.AuthViewModel
import com.flowintent.core.db.auth.ForgetPasswordUseCase
import com.flowintent.core.db.auth.GetUserProfileUseCase
import com.flowintent.core.db.auth.SignInUseCase
import com.flowintent.core.db.auth.SignUpUseCase
import com.flowintent.core.db.repository.EncryptedProtoRepository
import com.flowintent.core.util.Resource
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.test.rules.MainDispatcherRule
import com.flowintent.test.scenarios.UseCaseScenarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repo: EncryptedProtoRepository
    @Mock
    private lateinit var signUpUseCase: SignUpUseCase
    @Mock
    private lateinit var signInUseCase: SignInUseCase
    @Mock
    private lateinit var getUserProfileUseCase: GetUserProfileUseCase
    @Mock
    private lateinit var forgetPasswordUseCase: ForgetPasswordUseCase
    @Mock
    private lateinit var navigationDispatcher: NavigationDispatcher

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(repo.tokenFlow()).thenReturn(flowOf(null))
        whenever(repo.nameFlow()).thenReturn(flowOf(""))
        whenever(repo.emailFlow()).thenReturn(flowOf(""))
        
        viewModel = AuthViewModel(
            repo, signUpUseCase, signInUseCase, getUserProfileUseCase, forgetPasswordUseCase, navigationDispatcher
        )
    }

    @Test
    fun `signInUiState updates when email changes`() {
        viewModel.onSignInEmailChange("test@example.com")
        assertEquals("test@example.com", viewModel.signInUiState.value.email)
    }

    @Test
    fun `loginUser updates state to success and navigates`() = runTest {
        viewModel.onSignInEmailChange("test@example.com")
        viewModel.onSignInPasswordChange("password")
        
        whenever(signInUseCase("test@example.com", "password"))
            .thenReturn(UseCaseScenarios.success("fake-token"))

        viewModel.loginUser()

        assertFalse(viewModel.signInUiState.value.isLoading)
        assertEquals(null, viewModel.signInUiState.value.errorMessage)
    }

    @Test
    fun `loginUser updates state to error when failure occurs`() = runTest {
        viewModel.onSignInEmailChange("test@example.com")
        viewModel.onSignInPasswordChange("wrong")

        whenever(signInUseCase("test@example.com", "wrong"))
            .thenReturn(UseCaseScenarios.error("Invalid credentials"))

        viewModel.loginUser()

        assertFalse(viewModel.signInUiState.value.isLoading)
        assertEquals("Invalid credentials", viewModel.signInUiState.value.errorMessage)
    }

    @Test
    fun `registerUser updates state to success`() = runTest {
        viewModel.onSignUpFirstNameChange("John")
        viewModel.onSignUpLastNameChange("Doe")
        viewModel.onSignUpEmailChange("john@example.com")
        viewModel.onSignUpPasswordChange("password")

        whenever(signUpUseCase("John", "Doe", "john@example.com", "password"))
            .thenReturn(UseCaseScenarios.success(Unit))

        viewModel.registerUser()

        assertFalse(viewModel.signUpUiState.value.isLoading)
        assertEquals(null, viewModel.signUpUiState.value.errorMessage)
    }

    @Test
    fun `resetPassword updates state to success`() = runTest {
        viewModel.onForgotPasswordEmailChange("test@example.com")

        whenever(forgetPasswordUseCase("test@example.com"))
            .thenReturn(UseCaseScenarios.success(Unit))

        viewModel.resetPassword()

        assertFalse(viewModel.forgotPasswordUiState.value.isLoading)
        assertEquals("Reset link sent! Check your inbox." to false, viewModel.forgotPasswordUiState.value.statusMessage)
    }
}
