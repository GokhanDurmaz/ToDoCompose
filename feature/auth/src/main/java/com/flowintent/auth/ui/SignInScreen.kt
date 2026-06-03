 /**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.auth.R
import com.flowintent.auth.ui.vm.AuthViewModel
import com.flowintent.uikit.util.VAL_12
import com.flowintent.uikit.util.VAL_16
import com.flowintent.uikit.util.VAL_20
import com.flowintent.uikit.util.VAL_32
import com.flowintent.uikit.util.VAL_40
import com.flowintent.uikit.util.VAL_60
import com.flowintent.uikit.util.isValidEmail

@Composable
fun SignInScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.signInUiState.collectAsStateWithLifecycle()
    val errorInvalidEmail = stringResource(R.string.error_invalid_email)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(VAL_32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SignInHeader()

        SignInForm(
            email = uiState.email,
            password = uiState.password,
            onEmailChange = viewModel::onSignInEmailChange,
            onPasswordChange = viewModel::onSignInPasswordChange,
            onForgotClick = viewModel::onForgotPasswordClicked
        )

        ErrorMessage(uiState.errorMessage)

        Spacer(modifier = Modifier.height(VAL_32.dp))

        SignInButton(
            isLoading = uiState.isLoading,
            isEnabled = uiState.isSubmitEnabled,
            onClick = {
                if (!uiState.email.isValidEmail()) {
                    viewModel.setSignInError(errorInvalidEmail)
                } else {
                    viewModel.loginUser()
                }
            }
        )
        SignUpFooter(onNavigate = viewModel::onSignUpClicked)
    }
}

@Composable
private fun SignInHeader() {
    Spacer(modifier = Modifier.height(VAL_60.dp))
    Text(
        stringResource(R.string.welcome_back),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
    Text(
        stringResource(R.string.sign_in_subtitle),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(VAL_40.dp))
}

@Composable
private fun SignInForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onForgotClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(VAL_20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(VAL_16.dp), verticalArrangement = Arrangement.spacedBy(VAL_16.dp)) {
            CustomTextField(
                value = email,
                onValueChange = onEmailChange,
                label = stringResource(R.string.email_address),
                modifier = Modifier.fillMaxWidth()
            )
            CustomTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth(),
                isPassword = true
            )
            TextButton(
                onClick = onForgotClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    stringResource(R.string.forgot_password),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun SignInButton(isLoading: Boolean, isEnabled: Boolean, onClick: () -> Unit) {
    val gradient = Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(VAL_60.dp)
            .clip(RoundedCornerShape(VAL_12.dp))
            .background(if (isEnabled) gradient else Brush.linearGradient(listOf(Color.Gray, Color.LightGray))),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(
                stringResource(R.string.sign_in),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun ErrorMessage(message: String?) {
    message?.let {
        Text(
            it,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun SignUpFooter(onNavigate: () -> Unit) {
    Spacer(modifier = Modifier.height(VAL_16.dp))
    TextButton(onClick = onNavigate) {
        Text(
            stringResource(R.string.dont_have_account_sign_up),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
