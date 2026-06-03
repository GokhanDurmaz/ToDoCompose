/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.core.util.Resource
import com.flowintent.profile.ui.vm.ProfileViewModel
import com.flowintent.uikit.util.VAL_12
import com.flowintent.uikit.util.VAL_16

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.changePasswordState) {
        when (uiState.changePasswordState) {
            is Resource.Success -> {
                viewModel.clearChangePasswordState()
                viewModel.onBackClicked()
            }
            is Resource.Error -> {
                snackbarHostState.showSnackbar(
                    message = (uiState.changePasswordState as Resource.Error).message,
                    duration = SnackbarDuration.Short
                )
                viewModel.clearChangePasswordState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Change Password", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackClicked() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(VAL_16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(VAL_16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(VAL_16.dp)) {
                    Text(
                        text = "Update Security",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(VAL_16.dp))

                    PasswordTextField(
                        value = uiState.oldPassword,
                        onValueChange = viewModel::onOldPasswordChange,
                        label = "Current Password",
                        enabled = uiState.changePasswordState !is Resource.Loading
                    )

                    Spacer(modifier = Modifier.height(VAL_12.dp))

                    PasswordTextField(
                        value = uiState.newPassword,
                        onValueChange = viewModel::onNewPasswordChange,
                        label = "New Password",
                        enabled = uiState.changePasswordState !is Resource.Loading
                    )

                    Spacer(modifier = Modifier.height(VAL_12.dp))

                    PasswordTextField(
                        value = uiState.confirmPassword,
                        onValueChange = viewModel::onConfirmPasswordChange,
                        label = "Confirm New Password",
                        enabled = uiState.changePasswordState !is Resource.Loading
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.changePassword() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(VAL_12.dp),
                enabled = uiState.changePasswordState !is Resource.Loading &&
                        uiState.newPassword.isNotEmpty() &&
                        uiState.newPassword == uiState.confirmPassword,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (uiState.changePasswordState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Update Password", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(VAL_16.dp))
        }
    }
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        shape = RoundedCornerShape(VAL_12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    )
}
