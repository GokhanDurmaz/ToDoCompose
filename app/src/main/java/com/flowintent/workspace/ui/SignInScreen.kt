package com.flowintent.workspace.ui

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
import com.flowintent.core.util.Resource
import com.flowintent.workspace.ui.vm.AuthViewModel
import com.flowintent.workspace.util.COLOR_0XFF0F0F1C
import com.flowintent.workspace.util.COLOR_0XFF1A1A2E
import com.flowintent.workspace.util.COLOR_0XFF7B2FF7
import com.flowintent.workspace.util.COLOR_0XFF9D4EDD
import com.flowintent.workspace.util.COLOR_0XFFE63946
import com.flowintent.workspace.util.VAL_12
import com.flowintent.workspace.util.VAL_16
import com.flowintent.workspace.util.VAL_20
import com.flowintent.workspace.util.VAL_32
import com.flowintent.workspace.util.VAL_40
import com.flowintent.workspace.util.VAL_60
import com.flowintent.workspace.util.isValidEmail
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit = {},
    onSuccessLogin: () -> Unit = {}
) {
    val email by viewModel.emailInput.collectAsStateWithLifecycle()
    val password by viewModel.passwordInput.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isSubmitEnabled by viewModel.isSubmitEnabled.collectAsStateWithLifecycle()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(COLOR_0XFF0F0F1C))
            .verticalScroll(rememberScrollState())
            .padding(VAL_32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SignInHeader()

        SignInForm(
            email = email,
            password = password,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange
        )

        ErrorMessage(errorMessage)

        Spacer(modifier = Modifier.height(VAL_32.dp))

        SignInButton(
            isLoading = isLoading,
            isEnabled = isSubmitEnabled,
            onClick = {
                if (!email.isValidEmail()) {
                    errorMessage = "Please enter a valid email address"
                } else {
                    scope.launch {
                        handleLogin(viewModel, onSuccessLogin) { errorMessage = it }
                    }
                }
            }
        )

        SignUpFooter(onNavigateToSignUp)
    }
}

// --- Alt Bileşenler ---

@Composable
private fun SignInHeader() {
    Spacer(modifier = Modifier.height(VAL_60.dp))
    Text(
        "Welcome Back",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
    Text(
        "Please sign in to continue",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray
    )
    Spacer(modifier = Modifier.height(VAL_40.dp))
}

@Composable
private fun SignInForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(VAL_20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(COLOR_0XFF1A1A2E))
    ) {
        Column(modifier = Modifier.padding(VAL_16.dp), verticalArrangement = Arrangement.spacedBy(VAL_16.dp)) {
            CustomTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Email Address",
                modifier = Modifier.fillMaxWidth()
            )
            CustomTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Password",
                modifier = Modifier.fillMaxWidth(),
                isPassword = true
            )
            TextButton(onClick = { }, modifier = Modifier.align(Alignment.End)) {
                Text("Forgot Password?", color = Color.Gray, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun SignInButton(isLoading: Boolean, isEnabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier.fillMaxWidth().height(VAL_60.dp).clip(RoundedCornerShape(VAL_12.dp))
            .background(Brush.linearGradient(listOf(Color(COLOR_0XFF7B2FF7), Color(COLOR_0XFF9D4EDD)))),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        }
        else {
            Text("Sign In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ErrorMessage(message: String?) {
    message?.let {
        Text(
            it,
            color = Color(COLOR_0XFFE63946),
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
            "Don't have an account? Sign Up",
            color = Color(COLOR_0XFF9D4EDD),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private suspend fun handleLogin(
    viewModel: AuthViewModel,
    onSuccess: () -> Unit,
    onError: (String?) -> Unit
) {
    viewModel.loginUserWithState().collect { resource ->
        when (resource) {
            is Resource.Loading -> onError(null)
            is Resource.Success -> {
                if (resource.data.isNotEmpty()) viewModel.saveToken(resource.data)
                onSuccess()
            }
            is Resource.Error -> onError(resource.message)
        }
    }
}
