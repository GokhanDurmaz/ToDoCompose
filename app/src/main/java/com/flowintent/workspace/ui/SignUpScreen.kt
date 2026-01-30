package com.flowintent.workspace.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import com.flowintent.core.db.AuthCallbacks
import com.flowintent.core.db.SignUpState
import com.flowintent.core.util.Resource
import com.flowintent.workspace.ui.vm.AuthViewModel
import com.flowintent.workspace.util.COLOR_0XFF0F0F1C
import com.flowintent.workspace.util.COLOR_0XFF1A1A2E
import com.flowintent.workspace.util.COLOR_0XFF1E1E2F
import com.flowintent.workspace.util.COLOR_0XFF7B2FF7
import com.flowintent.workspace.util.COLOR_0XFF9D4EDD
import com.flowintent.workspace.util.COLOR_0XFFE63946
import com.flowintent.workspace.util.VAL_12
import com.flowintent.workspace.util.VAL_16
import com.flowintent.workspace.util.VAL_20
import com.flowintent.workspace.util.VAL_32
import com.flowintent.workspace.util.VAL_40
import com.flowintent.workspace.util.VAL_60
import com.flowintent.workspace.util.VAL_8
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit = {},
    onSuccessRegistration: () -> Unit = {}
) {
    var signUpState by remember { mutableStateOf(SignUpState()) }
    var isLoading by remember { mutableStateOf(false) }
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
        SignUpHeader()

        SignUpForm(
            state = signUpState,
            onStateChange = { signUpState = it }
        )

        ErrorMessage(errorMessage)

        Spacer(modifier = Modifier.height(VAL_32.dp))

        SignUpButton(
            isLoading = isLoading,
            onClick = {
                scope.launch {
                    handleRegistration(
                        viewModel = viewModel,
                        state = signUpState,
                        callbacks = AuthCallbacks(
                            onLoading = { isLoading = it },
                            onError = { errorMessage = it },
                            onSuccess = onSuccessRegistration
                        )
                    )
                }
            }
        )

        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Sign In", color = Color(COLOR_0XFF9D4EDD))
        }
    }
}

@Composable
private fun SignUpHeader() {
    Spacer(modifier = Modifier.height(VAL_40.dp))
    Text(
        "Create New Account",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
    Text("Join us and start your journey", color = Color.Gray)
    Spacer(modifier = Modifier.height(VAL_40.dp))
}

@Composable
private fun SignUpForm(
    state: SignUpState,
    onStateChange: (SignUpState) -> Unit
) {
    Card(
        shape = RoundedCornerShape(VAL_20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(COLOR_0XFF1A1A2E))
    ) {
        Column(modifier = Modifier.padding(VAL_16.dp), verticalArrangement = Arrangement.spacedBy(VAL_16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(VAL_8.dp)) {
                CustomTextField(
                    value = state.firstName,
                    onValueChange = { onStateChange(state.copy(firstName = it)) },
                    label = "First Name",
                    Modifier.weight(1f)
                )
                CustomTextField(
                    value = state.lastName,
                    onValueChange = { onStateChange(state.copy(lastName = it)) },
                    label = "Last Name",
                    Modifier.weight(1f)
                )
            }
            CustomTextField(
                value = state.email,
                onValueChange = { onStateChange(state.copy(email = it)) },
                label = "Email Address",
                Modifier.fillMaxWidth()
            )
            CustomTextField(
                value = state.password,
                onValueChange = { onStateChange(state.copy(password = it)) },
                label = "Password",
                Modifier.fillMaxWidth(),
                isPassword = true
            )
        }
    }
}

@Composable
private fun SignUpButton(isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
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
        } else {
            Text("Sign Up", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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

private suspend fun handleRegistration(
    viewModel: AuthViewModel,
    state: SignUpState,
    callbacks: AuthCallbacks
) {
    viewModel.registerUser(state.firstName, state.lastName, state.email, state.password).collect { resource ->
        when (resource) {
            is Resource.Loading -> {
                callbacks.onLoading(true)
                callbacks.onError(null)
            }
            is Resource.Success -> {
                callbacks.onLoading(false)
                viewModel.saveUser(state.firstName, state.lastName, state.email)
                callbacks.onSuccess()
            }
            is Resource.Error -> {
                callbacks.onLoading(false)
                callbacks.onError(resource.message)
            }
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = Color.Gray,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(COLOR_0XFF1E1E2F), RoundedCornerShape(VAL_12.dp))
                .padding(horizontal = VAL_16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(label, color = Color.DarkGray)
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (isPassword)
                    androidx.compose.ui.text.input.PasswordVisualTransformation()
                else androidx.compose.ui.text.input.VisualTransformation.None
            )
        }
    }
}
