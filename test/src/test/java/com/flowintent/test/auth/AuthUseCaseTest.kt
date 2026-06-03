/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.auth

import com.flowintent.core.db.auth.ChangePasswordUseCase
import com.flowintent.core.db.auth.ForgetPasswordUseCase
import com.flowintent.core.db.auth.GetUserProfileUseCase
import com.flowintent.core.db.auth.SignInUseCase
import com.flowintent.core.db.auth.SignUpUseCase
import com.flowintent.core.db.model.UserProfile
import com.flowintent.core.db.repository.AuthRepository
import com.flowintent.core.util.Resource
import com.flowintent.test.scenarios.UseCaseScenarios
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class AuthUseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var signUpUseCase: SignUpUseCase
    private lateinit var signInUseCase: SignInUseCase
    private lateinit var getUserProfileUseCase: GetUserProfileUseCase
    private lateinit var forgetPasswordUseCase: ForgetPasswordUseCase
    private lateinit var changePasswordUseCase: ChangePasswordUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        signUpUseCase = SignUpUseCase(authRepository)
        signInUseCase = SignInUseCase(authRepository)
        getUserProfileUseCase = GetUserProfileUseCase(authRepository)
        forgetPasswordUseCase = ForgetPasswordUseCase(authRepository)
        changePasswordUseCase = ChangePasswordUseCase(authRepository)
    }

    @Test
    fun `SignUpUseCase returns success when repository succeeds`() = runTest {
        val expected = Resource.Success(Unit)
        whenever(authRepository.registerUser("name", "surname", "email", "pass"))
            .thenReturn(UseCaseScenarios.success(Unit))

        signUpUseCase("name", "surname", "email", "pass").collect { result ->
            assertEquals(expected, result)
        }
    }

    @Test
    fun `SignInUseCase returns token when repository succeeds`() = runTest {
        val expected = Resource.Success("token")
        whenever(authRepository.loginUser("email", "pass"))
            .thenReturn(UseCaseScenarios.success("token"))

        signInUseCase("email", "pass").collect { result ->
            assertEquals(expected, result)
        }
    }

    @Test
    fun `GetUserProfileUseCase returns profile when repository succeeds`() = runTest {
        val profile = UserProfile(name = "John", surname = "Doe", email = "john@example.com")
        val expected = Resource.Success(profile)
        whenever(authRepository.getUserProfile())
            .thenReturn(UseCaseScenarios.success(profile))

        getUserProfileUseCase().collect { result ->
            assertEquals(expected, result)
        }
    }

    @Test
    fun `ForgetPasswordUseCase returns success when repository succeeds`() = runTest {
        val expected = Resource.Success(Unit)
        whenever(authRepository.forgetPassword("email"))
            .thenReturn(UseCaseScenarios.success(Unit))

        forgetPasswordUseCase("email").collect { result ->
            assertEquals(expected, result)
        }
    }

    @Test
    fun `ChangePasswordUseCase returns success when repository succeeds`() = runTest {
        val expected = Resource.Success(Unit)
        whenever(authRepository.changePassword("old", "new"))
            .thenReturn(UseCaseScenarios.success(Unit))

        changePasswordUseCase("old", "new").collect { result ->
            assertEquals(expected, result)
        }
    }
}
