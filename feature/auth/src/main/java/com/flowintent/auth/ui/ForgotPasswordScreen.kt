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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.auth.ui.vm.AuthViewModel
import com.flowintent.core.util.Resource
import com.flowintent.uikit.util.VAL_12
import com.flowintent.uikit.util.VAL_16
import com.flowintent.uikit.util.VAL_20
import com.flowintent.uikit.util.VAL_2000L
import com.flowintent.uikit.util.VAL_32
import com.flowintent.uikit.util.VAL_40
import com.flowintent.uikit.util.VAL_60
import com.flowintent.uikit.util.isValidEmail
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val email by viewModel.emailInput.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    var statusMessage by remember { mutableStateOf<Pair<String, Boolean>?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(VAL_32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderSection(onBack = viewModel::onNavigateBack)

        PasswordResetForm(
            email = email,
            onEmailChange = viewModel::onEmailChange
        )

        StatusIndicator(statusMessage)

        Spacer(modifier = Modifier.height(VAL_32.dp))

        ResetButton(
            isLoading = isLoading,
            isEnabled = email.isNotEmpty(),
            onClick = {
                if (!email.isValidEmail()) {
                    statusMessage = "Please enter a valid email address" to true
                } else {
                    scope.launch {
                        handlePasswordReset(viewModel, email) { statusMessage = it }
                    }
                }
            }
        )
    }
}

@Composable
private fun PasswordResetForm(email: String, onEmailChange: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(VAL_20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(VAL_16.dp), verticalArrangement = Arrangement.spacedBy(VAL_16.dp)) {
            Text(
                "Enter your email address and we will send you instructions to reset your password.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
            CustomTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Email Address",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ResetButton(isLoading: Boolean, isEnabled: Boolean, onClick: () -> Unit) {
    val gradient = Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    Button(
        onClick = onClick,
        enabled = !isLoading && isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(VAL_60.dp)
            .clip(RoundedCornerShape(VAL_12.dp))
            .background(if (isEnabled && !isLoading) gradient else Brush.linearGradient(listOf(Color.Gray, Color.LightGray))),
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
                "Send",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun StatusIndicator(status: Pair<String, Boolean>?) {
    status?.let { (message, isError) ->
        Text(
            text = message,
            color = if (isError) MaterialTheme.colorScheme.error else Color(0xFF4CAF50),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun HeaderSection(onBack: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(VAL_40.dp))
        TextButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(
                "< Back to Login",
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.height(VAL_20.dp))
        Text(
            "Forgot Password",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(VAL_40.dp))
    }
}

private suspend fun handlePasswordReset(
    authViewModel: AuthViewModel,
    email: String,
    onResult: (Pair<String, Boolean>) -> Unit
) {
    authViewModel.apply {
        forgetPassword(email).collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    onResult("Reset link sent! Check your inbox." to false)
                    delay(VAL_2000L)
                    authViewModel.onNavigateBack()
                }
                is Resource.Error -> onResult(resource.message to true)
                else -> Unit
            }
        }
    }
}

