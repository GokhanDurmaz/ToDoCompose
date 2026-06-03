/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.auth.ui.vm

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isSubmitEnabled: Boolean = email.isNotBlank() && password.isNotBlank() && !isLoading
}

data class SignUpUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isSubmitEnabled: Boolean = firstName.isNotBlank() && 
                                  lastName.isNotBlank() && 
                                  email.isNotBlank() && 
                                  password.isNotBlank() && 
                                  !isLoading
}

data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val statusMessage: Pair<String, Boolean>? = null
)

data class TwoFactorUiState(
    val code: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isSubmitEnabled: Boolean = code.length == 6 && !isLoading
}
