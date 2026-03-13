package com.flowintent.auth.ui

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
import com.flowintent.auth.ui.vm.AuthViewModel
import com.flowintent.core.db.model.SignUpState
import com.flowintent.core.util.Resource
import com.flowintent.uikit.util.VAL_12
import com.flowintent.uikit.util.VAL_16
import com.flowintent.uikit.util.VAL_20
import com.flowintent.uikit.util.VAL_32
import com.flowintent.uikit.util.VAL_40
import com.flowintent.uikit.util.VAL_60
import com.flowintent.uikit.util.VAL_8
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    var signUpState by remember { mutableStateOf(SignUpState()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                        onError = { errorMessage = it },
                        onLoading = { isLoading = it }
                    )
                }
            }
        )

        TextButton(onClick = viewModel::onNavigateBack) {
            Text(
                "Already have an account? Sign In",
                color = MaterialTheme.colorScheme.secondary
            )
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
        color = MaterialTheme.colorScheme.onBackground
    )
    Text(
        "Join us and start your journey",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
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
    val gradient = Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )

    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(VAL_60.dp)
            .clip(RoundedCornerShape(VAL_12.dp))
            .background(if (!isLoading) gradient else Brush.linearGradient(listOf(Color.Gray, Color.LightGray))),
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
                "Sign Up",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
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
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerHighest,
                    RoundedCornerShape(VAL_12.dp)
                )
                .padding(horizontal = VAL_16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (isPassword)
                    androidx.compose.ui.text.input.PasswordVisualTransformation()
                else androidx.compose.ui.text.input.VisualTransformation.None
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

private suspend fun handleRegistration(
    viewModel: AuthViewModel,
    state: SignUpState,
    onLoading: (Boolean) -> Unit,
    onError: (String?) -> Unit
) {
    viewModel.registerUser(state.firstName, state.lastName, state.email, state.password).collect { resource ->
        when (resource) {
            is Resource.Loading -> {
                onLoading(true)
                onError(null)
            }
            is Resource.Success -> {
                onLoading(false)
                viewModel.saveUser(state.firstName, state.lastName, state.email)
                viewModel.onNavigateBack()
            }
            is Resource.Error -> {
                onLoading(false)
                onError(resource.message)
            }
        }
    }
}
