/*
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
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
import com.flowintent.uikit.util.VAL_80

@Composable
fun TwoFactorScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.twoFactorUiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(VAL_32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TwoFactorHeader()

        TwoFactorForm(
            code = uiState.code,
            onCodeChange = viewModel::onTwoFactorCodeChange
        )

        ErrorMessage(uiState.errorMessage)

        Spacer(modifier = Modifier.height(VAL_32.dp))

        TwoFactorButton(
            isLoading = uiState.isLoading,
            isEnabled = uiState.isSubmitEnabled,
            onClick = viewModel::verifyTwoFactor
        )

        ResendFooter(onResend = viewModel::resendTwoFactorCode)
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
private fun TwoFactorHeader() {
    Spacer(modifier = Modifier.height(VAL_60.dp))
    Icon(
        imageVector = Icons.Default.PhoneAndroid,
        contentDescription = null,
        modifier = Modifier.size(VAL_80.dp),
        tint = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(VAL_32.dp))
    Text(
        stringResource(R.string.two_factor_title),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center
    )
    Text(
        stringResource(R.string.two_factor_subtitle),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 8.dp)
    )
    Spacer(modifier = Modifier.height(VAL_40.dp))
}

@Composable
private fun TwoFactorForm(
    code: String,
    onCodeChange: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(VAL_20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(VAL_16.dp),
            verticalArrangement = Arrangement.spacedBy(VAL_16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                value = code,
                onValueChange = { if (it.length <= 6) onCodeChange(it) },
                label = stringResource(R.string.verification_code),
                modifier = Modifier.fillMaxWidth()
            )
            
            Text(
                text = "Enter the 6-digit code",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TwoFactorButton(isLoading: Boolean, isEnabled: Boolean, onClick: () -> Unit) {
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
                stringResource(R.string.verify_and_proceed),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun ResendFooter(onResend: () -> Unit) {
    Spacer(modifier = Modifier.height(VAL_16.dp))
    TextButton(onClick = onResend) {
        Text(
            stringResource(R.string.resend_code),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
